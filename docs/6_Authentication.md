## Authentication
Don't forget that for postman and others you will need a bearer token.
To generate token you can either take it from unit tests or go to:
https://jwt.io/#debugger-io?token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJhc2tleGNoLXVzZXJzIiwiaXNzIjoiQXNrRXhjaGFuZ2VLb3RsaW4iLCJncm91cHMiOlsiVVNFUiIsIlRFU1QiXSwic3ViIjoidXNlcjEifQ.gWsMS1KbX9jB37r3JaKH2mXbydJveSiuvCFD5HKmKHo

Here are the parameters used for generation:

header: {
   "alg": "HS256",
   "typ": "JWT"
 }
 payload: {
   "aud": "askexch-users",
   "iss": "AskExchangeKotlin",
   "groups": [
     "USER",
     "TEST"
   ],
   "sub": "user1"
 }
 signature: secret1 (don't encode in base64!)
  