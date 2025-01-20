# Lotto-CLI
This is a very simple command line tool, which can be used to analyze an arbitrary number of lottery tips for the german lottery called "Lotto".

# Lottery rules
The German lottery offers 2 drawings a week. One on Wednesday, the other one on Saturay. Each time, a series of 6 numbers (between 1 and 49) will be drawn as well as an additional number from 0 to 9 (the so called super number). 
To participate in the lottery you need to guess those 6 numbers as well as the super number. You need to guess at least 2 numbers correctly plus the super number to win a prize (which is, at the moment of writing, 6€ per drawing). 
If you have guessed more numbers (e.g. 3 normal numbers but no super number) you will get more money, depending on the number of correct numbers and the total sum of money available in the jackpot.

# Purpose of this app
The German lottery offers an online service to check if a simple tip (6 numbers + super number) has won something or not for a certain date. But when you need to check a mulitude of tips against a longer time interval this is a very time consuming and error prone task.
You can use this simple app to pass in a whole CSV file of tips against another CSV file containing the drawing results, creating an excel output file containing everything you've won in that period of time.

# Quick start
The only thing you need to specify in order to have a first, quick run are the tips you want to check.
For this, you need to create a CSV file of the following format (with <LINE_1> to <LINE_N> just for illustration purposes and NOT being part of the file):

    <LINE_1> number1;number2;number3;number4;number5;number6;supernumber;
    <LINE_2> number1;number2;number3;number4;number5;number6;supernumber;
    ...
    <LINE_N> number1;number2;number3;number4;number5;number6;supernumber;

If you than start the analyzing process via
    java -jar <YOUR_JAR.JAR> -t <YOUR_FILE.CSV>

the tips will automatically be analyzed against an example file containing all drawings of the year 2024.
Of course, you can also specify your own file containing more drawing results. This can be done via the -d parameter.
Please call the application without any parameters or with the --help flag in order to get a full list of available parameters.