                                Gaming MarketPlace

API documentation available at http://localhost:8080/swagger-ui/index.html

Assumptions while creating the project

* A user can sell multiple units of same item to another user. So listing will only contain single item that can have multiple units. 
* One user at a time can buy partially or completely from listing. So If User A has listed
4 guns for sale , User B can buy 2 units of gun or he can buy all 4.
* The account balance field in account table is the balance of user in the gaming account. As he is paying through 
credit card, no checks and debit is done from his account balance. However for the seller
amount is added in this account only.
* Similarly for the company we have one single account with name "SUPAKI" which receives 10% from each sale. This account is added during
application load time.

Entities created in the project

* Item 
* Account 
* Account Item Mapping
* Listing
* Transaction








