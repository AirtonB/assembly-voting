INSERT INTO schedule (description) VALUES ('Schedule #1');

INSERT INTO schedule (description) VALUES ('Schedule #2');

INSERT INTO session(start_session, end_session, schedule_id)
VALUES (CURRENT_TIMESTAMP,  DATEADD(MINUTE, +1, CURRENT_TIMESTAMP), 1);
INSERT INTO vote(registered_vote, user_identification, schedule_id)	VALUES (true,'91093350059', 1);

INSERT INTO session(start_session, end_session, schedule_id)
VALUES ('1994-04-19 07:00:00',  '1994-04-19 06:01:00', 2);

INSERT INTO vote(registered_vote, user_identification, schedule_id)	VALUES (true,'91093350059', 2);
INSERT INTO vote(registered_vote, user_identification, schedule_id) VALUES (true,'12914127006', 2);
INSERT INTO vote(registered_vote, user_identification, schedule_id) VALUES (true,'91492806005', 2);
INSERT INTO vote(registered_vote, user_identification, schedule_id) VALUES (false,'25040603002', 2);
INSERT INTO vote(registered_vote, user_identification, schedule_id) VALUES (false,'03579468049', 2);
