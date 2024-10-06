Run the DB Queries specified in the text;

    After running queries check for the following

    	1. Needs to have a new user created as applova_temp with Create DB permission
    	2. Needs to have two databases (restaurant & restaurant_test) both with new created owner applova_temp;
    	3. Connect to the DB


Clone source code from git HTTPS:[https://github.com/charinDperera/applova.git] - branch [assessment1]

Either run from source code with IDE or build using maven build and run the jar file.

The API(ex: http://localhost:8080/api/orders) will allow:

    (POST)To create an Order request body json looks like:
    	Where entering product list and waiter name is a must, and discount taxes and serviceCharge can be left empty, and will be assumed to be 0.
    (GET)To get a page of all orders made.
    	Where u need to enter the page number and page size as query parameters
