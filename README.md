** This application uses the library 'twitter4j' to integrate with Twitter and access the streaming api.
* This application aims to find ​out ​how ​the ​number ​of ​mentions ​of ​a ​company ​on ​Twitter ​change ​over ​time.

* The run method aims to count the tweets for a keyword and report how it changes over time.
* In the 'run' method of the class TwitterScanner, StatusListener is implemented and added as a listener.
StatusListener uses twitter streaming api to listen to the live feeds. The tweets are then filtered and the
'onStatus' method is called for every tweet.
*
* OAuth access token details are to be stored in a properties file.
*
* The search string is stored as a static variable 'SEARCH_STRING'.
* The initial tweet count is set as 100. This value will be compared with the tweet count after an hour(or whatever
period is chosen)
* The monitor interval is store as 'MONITOR_INTERVAL_IN_SECONDS'. The period for which tweets are to counted are to be
set to this variable in seconds.
* The application currently runs for a period of time and then exits. The period is set in the variable
'TOTAL_MONITOR_DURATION_IN_SECONDS' in seconds.
*
* Currently, the application will exit after 3 minutes. It counts tweets for 30 seconds and reports a percentage
increase or decrease on the console. The initial tweet count value is taken as 100.
