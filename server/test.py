#!/usr/bin/python3
import requests
import json
from random import randint

def hit(endpoint, data):
    if endpoint not in ('/register', '/login', '/join', '/clear'):
        data = {'sessionId': data['sessionId']}

    response = json.loads(requests.post('http://localhost:8080' + endpoint, data=json.dumps(data)).text)
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
hit('/create', model1)

# test join
user2 = users.__next__()
model2 = hit('/register', user2)
hit('/join', {'sessionId': model2['sessionId'],
              'gameId': model2['availableGames'][0]['gameId']})

# test leave
model2 = hit('/leave', model2)

# test start
hit('/join', {'sessionId': model2['sessionId'],
              'gameId': model2['availableGames'][0]['gameId']})
hit('/start', model2)

# test state
hit('/state', model1)

