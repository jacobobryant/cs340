#!/usr/bin/python3
import unittest
import requests
import json
from copy import deepcopy
from random import randint

def assoc(m, *args):
    assert len(args) % 2 == 0
    ret = deepcopy(m)
    for i in range(0, len(args), 2):
        ret[args[i]] = args[i+1]
    return ret

def select(d, *keys):
    return {k: d[k] for k in keys}

def hit(endpoint, data):
    response = json.loads(requests.post('http://localhost:8080' + endpoint, data=json.dumps(data)).text)
    print(endpoint)
    print_json(data)
    print_json(response)
    print()

    if "code" in response:
        if response["message"] == "internal server error":
            raise Exception("internal server error")
        assert False

    return response

def print_json(data):
    _data = deepcopy(data)
    try:
        _data["currentGame"]["openRoutes"] = "[removed by test.py]"
    except (KeyError, TypeError) as e:
        pass
    print(json.dumps(_data, indent=2))

def gen_user():
    n = 0
    while True:
        yield {'username': 'user' + str(n), 'password': '12345'}
        n += 1
users = gen_user()

route = {"city1": "Seattle",
         "city2": "Vancouver",
         "type": "any",
         "length": 1,
         "second": True}

class TestServer(unittest.TestCase):

    def run(self, result=None):
        if not (result.failures or result.errors):
            super().run(result)

    @staticmethod
    def new_user():
        return select(hit('/register', users.__next__()), 'sessionId')

    @staticmethod
    def join_first(user):
        request = deepcopy(user)
        request['gameId'] = hit('/state', user)['availableGames'][0]['gameId']
        hit('/join', request)

    def init_game(self, finish_init=False):
        self.join_first(self.user2)
        hit('/start', self.user1)
        if finish_init:
            hit('/return-dest', assoc(self.user1, 'cards', []))
            hit('/return-dest', assoc(self.user2, 'cards', []))
        else:
            self.pending = hit('/state', self.user1)["currentGame"]["players"][0]["pending"]

    def raises(self, fn, *args, **kwargs):
        self.assertRaises(AssertionError, fn, *args, **kwargs)

    def setUp(self):
        hit('/clear', None)
        self.user1 = self.new_user()
        self.user2 = self.new_user()
        self.user3 = self.new_user()
        hit('/create', self.user1)

    def test_register(self):
        u = users.__next__()
        hit('/register', u)
        self.raises(lambda: hit('/register', u))

    def test_login(self):
        u = users.__next__()
        assert "currentGame" in hit('/register', u)
        hit('/login', u)
        hit('/login', u)

    def test_join_leave(self):
        self.join_first(self.user2)
        hit('/leave', self.user2)

    def test_start(self):
        self.raises(lambda: hit('/start', self.user1))
        self.join_first(self.user2)
        hit('/start', self.user1)

    def test_return_dest(self):
        self.init_game()

        request = deepcopy(self.user1)
        request['cards'] = self.pending[:1]

        request['cards'][0]['points'] += 1
        self.raises(lambda: hit('/return-dest', request))
        request['cards'][0]['points'] -= 1

        request['cards'] = self.pending[:2]
        self.raises(lambda: hit('/return-dest', request))

        request['cards'] = self.pending[:1]
        hit('/return-dest', request)

        request['cards'] = self.pending[1:2]
        self.raises(lambda: hit('/return-dest', request))

        request = deepcopy(self.user2)
        request['cards'] = []
        hit('/return-dest', request)

    def test_chat(self):
        message = "hello there"
        state = hit('/chat', assoc(self.user1, "message", message))
        hit('/chat', assoc(self.user1, "message", 1))
        assert message in state['currentGame']['messages'][0]
        self.raises(lambda: hit('/chat', assoc(self.user2, "message", "foo")))

    def test_faceup_init(self):
        self.join_first(self.user2)
        state = hit('/start', self.user1)
        assert len(state['currentGame']['faceUpDeck']) == 5

    def test_draw_dest(self):
        self.init_game(True)
        hit('/draw-dest', self.user1)

    def test_draw_train(self):
        self.init_game(True)
        hit('/draw-train', self.user1)
        hit('/draw-train', self.user1)
        hit('/draw-faceup-train', assoc(self.user2, "index", 0))
        hit('/draw-faceup-train', assoc(self.user2, "index", 0))

    def test_build_train(self):
        self.init_game(True)
        cards = hit('/state', self.user1)['currentGame']['players'][0]['trainCards']

        self.raises(lambda: hit('/build', assoc(self.user1,
                            "route", route, "cards", [])))
        self.raises(lambda: hit('/build', assoc(self.user1,
                            "route", route, "cards", cards)))
        hit('/build', assoc(self.user1, "route", route, "cards", cards[:1]))

        cards = hit('/state', self.user2)['currentGame']['players'][1]['trainCards']
        self.raises(lambda: hit('/build', assoc(self.user2, "route", 
            assoc(route, "second", False), "cards", cards[:1])))

    def test_update_score(self):
        self.init_game(True)
        cards = hit('/state', self.user1)['currentGame']['players'][0]['trainCards']
        hit('/build', assoc(self.user1, "route", route, "cards", cards[:1]))

if __name__ == "__main__":
    unittest.main()
