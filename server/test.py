#!/usr/bin/python3
import requests
import json

def hit(endpoint, data):
    return json.loads(requests.post('http://localhost:8080' + endpoint, data=json.dumps(data)).text)

def print_json(data):
    print(json.dumps(data, indent=2))

user = {'username': 'john', 'password': '12345'}
sid_data = {'sessionId': hit('/register', user)['sessionId']}
hit('/create', sid_data)
hit('/login', user)
print_json(hit('/state', sid_data))
