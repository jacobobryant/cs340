Testing plan
- /login
  - equivilance classes
    - username and password correct
      - request {"username": "fred",
                 "password": "12345"}
      - response contains a new valid sessionId as well as other normal client model data
    - username missing
      - response {"error": "username is missing",
                  "code": 10}
    - password missing
      - response {"error": "password is missing",
                  "code": 11}

- /register
  - acts same as login except this case
    - username already taken
      - {"error": "username [username] already taken"
        "code": 13}

- all requests other than those 2 include a sessionId, they are tested in this way
  - equivilance classes
    - correct sessionId
      - response contains client model data
    - incorrect/invalid sessionId
      - response {"error": "invalid sessionId"
                 "code": 14}

- /join
  - correct gameId
    - full game
      - response {"error": "game [gameId] is full",
                 "code": 15}
    - joinable game
      - response client model data with joined game data
  - incorrect gameId
    - response {"error": "invalid gameId",
               "code": 16}

- /leave
  - the only cases are that this sessionId is part of a game or is not
    - part of game
      - response client model updated to not include that game
    - not part of a game
      - response {"error": "this sessionId is not part of a game",
                  "code": 16}

- /create
  - already part of a game
    - response {"error": "this sessionId is already part of a game",
               "code": 17}
  - not part of a game
    - response client model with change that they are part of a game now

- /start
  - already part of a game
    - not enough players
      - response {"error": "not enough players in game",
                 "code": 18}
    - correct enough players
      - response client model with this game flagged as started
  - not part of a game
    - response {"error": "this sessionId is not part of a game",
               "code": 16}

- /state
  - response client model
