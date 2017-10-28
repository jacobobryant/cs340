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
    assert "code" not in response
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
        hit('/register', u)
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
        self.join_first(self.user2)
        hit('/start', self.user1)

        cards = hit('/state', self.user1)["currentGame"]["players"][0]["destCards"]
        request = deepcopy(self.user1)
        request['dest'] = cards[0]

        hit('/return-dest', self.user1)

        request['dest']['points'] += 1
        self.raises(lambda: hit('/return-dest', request))

        request['dest']['points'] -= 1
        hit('/return-dest', request)

        request['dest'] = cards[1]
        self.raises(lambda: hit('/return-dest', request))

        hit('/return-dest', self.user2)

    def test_chat(self):
        message = "hello there"
        state = hit('/chat', assoc(self.user1, "message", message))
        assert message in state['currentGame']['messages'][0]
        self.raises(lambda: hit('/chat', self.user1))
        self.raises(lambda: hit('/chat', assoc(self.user2, "message", "foo")))

if __name__ == "__main__":
    unittest.main()
