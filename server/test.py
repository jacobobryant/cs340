#!/usr/bin/python3
import requests
import json
from random import randint

def select(d, *keys):
    return {k: d[k] for k in keys}

def hit(endpoint, data):
    response = json.loads(requests.post('http://localhost:8080' + endpoint, data=json.dumps(data)).text)
    try:
        response["currentGame"]["openRoutes"] = "[removed by test.py]"
    except (KeyError, TypeError) as e:
        pass
    print(endpoint)
    print_json(data)
    print_json(response)
    print()
    assert "code" not in response
    return response

def print_json(data):
    print(json.dumps(data, indent=2))

def gen_user():
    n = 0
    while True:
        yield {'username': 'user' + str(n), 'password': '12345'}
        n += 1
users = gen_user()

hit('/clear', None)
# test register
user1 = users.__next__()
model1 = hit('/register', user1)

# test login
hit('/login', user1)

# test create
hit('/create', select(model1, 'sessionId'))

# test join
user2 = users.__next__()
model2 = hit('/register', user2)
hit('/join', {'sessionId': model2['sessionId'],
              'gameId': model2['availableGames'][0]['gameId']})

# test leave
model2 = hit('/leave', select(model2, 'sessionId'))

# test start
hit('/join', {'sessionId': model2['sessionId'],
              'gameId': model2['availableGames'][0]['gameId']})
hit('/start', select(model2, 'sessionId'))

# test state
client_model = hit('/state', select(model1, 'sessionId'))

cards = client_model["currentGame"]["players"][0]["destCards"]
model1['dest'] = cards[0]
hit('/return-dest', select(model1, 'sessionId', 'dest'))

model1['dest'] = cards[1]
try:
    hit('/return-dest', select(model1, 'sessionId', 'dest'))
    assert False
except AssertionError:
    pass

#model2 = hit('/state', select(model2, 'sessionId'))
try:
    hit('/return-dest', select(model2, 'sessionId'))
    assert False
except AssertionError:
    pass

model2['dest'] = None
hit('/return-dest', select(model2, 'sessionId', 'dest'))
