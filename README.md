# Travel-Fares

## About the project

This is a springboot project built with gradle. I have created a springboot project with plan to extend it for batch
processing or extend the app with an api for Trips. But due to time constraint, I could not finish all that I have planned.

## How to run the application

1. Clone the project to a directory.
2. Once cloned build the project using the command './gradlew clean build'
3. This should build the project and generate an executable jar **_travel-fares-1.0.0.jar_** under _project-dir/build/libs_
4. sample test input file  test-tap-data.csv has already been placed for reference.
5. Now from the project directory, run  java -jar build/libs/travel-fares-1.0.0.jar test-tap-data.csv
6. This will generate an output file <UUID>.csv. (please always check for the latest one as I couldn't write the clearing old files logic)


Or from the project directory, run `gradle clean build `and the test will automatically run.

## Design Decisions and Assumptions

1. I have designed stopIds to be Integers so that I can have one single record for a fare between 2 stops. The fare
   record will have a smaller stop as the source and bigger stop as the destination.
2. I have persisted fares, taps and trips for any future extensions like reporting or troubleshooting.
3. I have assumed that both TapOn and TapOff can be forgotten and they will result in a trip with maximum fare for the
   tapped stop.
4. I have maintained fares in a in memory DB so that it is easier to fetch it on demand. If there is any change expected
   in the fares for a given test input file, please update _**src/main/resources/data.sql**_
5. If there are no fares found for given stops the charge amount will be 0.