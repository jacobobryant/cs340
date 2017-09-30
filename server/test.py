#!/usr/bin/python3
import requests
import json

def hit(endpoint, data):
    return json.loads(requests.post('http://localhost:8080' + endpoint, data=json.dumps(data)).text)

user = {'username': 'john', 'password': '12345'}
sid = hit('/register', user)['sessionId']
hit('/create', {'sessionId': sid})
hit('/login', user)
