# Travel-Fares

## About the project
This is a springboot project built with gradle. I have created a springboot project with plan to extend it for batch processing
or extend the app with an api for Trips. But due to time constraint, I could not finish all that I have planned. 

## How to run the application
As I could not finish the full application, I have created a test that takes an input file from the project, processes the tap data and logs all the generated trips on the console. 
1. Load the project in the IDE as a gradle project
2. Run **_TapDataFileProcessorTest_** and then it reads the input file from src/test/resources
3. It logs the generated Trips on the console

Or from the project directory, run `gradle clean build `and the test will automatically run. 

## Design Decisions and Assumptions
1. I have designed stopIds to be Integers so that I can have one single record for a fare between 2 stops. The fare record will have a smaller stop as the source and bigger stop as the destination.
2. I have persisted fares, taps and trips for any future extensions like reporting or troubleshooting. 
3. I have assumed that both TapOn and TapOff can be forgotten and they will result in a trip with maximum fare for the tapped stop. 
4. I have maintained fares in a in memory DB so that it is easier to fetch it on demand. If there is any change expected in the fares for a given test input file, please update _**src/main/resources/data.sql**_
