# MVP

- Since AskExchange is to trade answers, MVP will require 2 endpoints:
1) **/v1/question** endpoint to:\
POST, READ, DELETE and UPDATE a question
2) **/v1/answer** endpoint to:\
  POST, READ, DELETE and UPDATE an answer
- READing a question will also pull all the related answers. READing an answer will just pull an answer

- What about user creation/registration?\
We need a way to CREATE a user, since questions and answers have to belong to someone after all.
We could've added **/v1/register** endpoint and implemented registration for MVP.
But can't afford another entity yet. User will be hardcoded as an "admin" both for answers and questions.

So there will be a TABLES:
- Table of USERS with
1) LOGIN (will serve as a USERNAME also)
2) PASSWORD
3) EMAIL
4) AREA OF EXPERTISE (front-end, back-end, fullstack, devops)
- Table of QUESTIONS with
1) QUESTION TITLE
2) QUESTION BODY
3) LOGIN (an owner's username)
- Table of ANSWERS with
1) ANSWER BODY (obviously, when we load a thread we will pull all answers having a particular QUESTION TITLE)
2) QUESTION TITLE
