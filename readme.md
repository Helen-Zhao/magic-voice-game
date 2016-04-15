
#Introduction
This program is a "magic voice game", whereupon a new user can register a username, login and play a simple dice rolling game. The user can also view statistics on their previous games, and other users games to see where they stand on the leaderboards. A text-to-speech prompt will also be called upon visiting the home page, announcing the results of a game and announcing statistics.

This program uses Java Swing GUI and Linux bash commands for festival calls and process identification/killing. 
To be able to run this program, you must be on Linux and have festival installed. (See https://wiki.archlinux.org/index.php/Festival for more info).

#Functional Requirements
##Register User
To register a user, a username must be entered that is not already taken. If the username is already taken, the user will be prompted to enter a another username. The username is then stored to a txt file.

##Logging in
A username must be used to log in. No passwords are required. All of a user's previous game data are stored to the username as a key. Upon logging in, the program will say "Welcome <username>, let's play!"

##Playing the game
When the user decides to play the game, the program allows the user to enter a valid number between 1 and 6 inclusive. The program then reports whether it was a correct guess or not. If the guess was correct, the program will say "Well done <username>, you guessed correctly”. If the guess was incorrect, the program will say “Sorry <username>. You guessed <guess>, but it was actually <actual>”.

##Statistics/leaderboards
The stats should be displayed for ALL registered users, like a “leader board”. It does not matter how you decide who the “top” user is (e.g. most games played, most correct guesses, etc), but make it reasonable. The important aspect of this feature is that:

• The stats are displayed for all registered users all the time (not only the logged-in user).

• The stats should be sorted according to some criteria. You decide what that is, but make it obvious.

• Every user will have 2 buttons next to their name (although you can achieve the same thing with one button if you want). When the button is first clicked, Festival will start to speak “(Username) has a score of (score) and is in (position) place on the leader board with a total (num-users) players registered for the magic number game”. The actual contents of what is spoken is not the most essential part (although having it in one long sentence without using any fullstops may simplify things for you), but it should mention the username and respective score and require at least 5 seconds to speak.

This is because there are 2 important aspects you need to implement here:

– The GUI should never freeze while it is saying the long expression.

– The player might decide to press the speak function for 2-3 players at the same time (in which case the Festival voices will be overlapping).

– You should provide the user with the ability to cancel (the “second button”) the speech mid-way as it for that particular user. This means you need to figure out the process ID of the process(es) spawned by the speech command (remember – this needs to be a cancel per username, not global).

This means you CANNOT use the killall command. You must figure out the particular process

– As an example, we will press the “speak” button for 2 different users. Your app will speak the content for all 2 users simultaneously. But when we press cancel for one of those users, the stats speaking for the other user will remain unaffected.
