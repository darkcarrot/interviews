## Assumption
0. When the system starts up, some other sources provides static data, such as current inventory, member data and current loan. See `LibrarySystemTest` for an example
1. There is search system, returning *item id* given the *item name*
2. A member borrow an item by providing a *name* and the *member id*
3. A *unique item* with this item will be lend if it is available
4. A member can only borrow on the same day, i.e. the *borrow date* is always current date when an item is issued/no advance borrow/reservation
5. The *unique Id* is known when a item was returned
6. Once return, the *loan* record is removed, which can be saved for future reference if required
7. `LibrarySystem.checkInventory` returns items with 0 inventories
8. There is a data error in the sample provided. The followings have the same *book id*
    9,5,DVD,The Imitation Game
    10,5,VHS,WarGames


## Design
0. It is a Maven project
1. Based on the csv file, the library can hold multiple copies of a single item. 
    Therefore `Item` is used to represent a distinct book/DVD/etc, while `UniqueItem` is a wrapper with a *unique id* 
2. `Member` holds basic information, including the *member id* and *name*
3. `Loan` represents a record for lending of a unique item, associated with a *member* and the *date* it is brought. 
    The *due date* is generated based on *borrow date* when a loan object is initialised
4. `LibrarySystem` is the main class handles all actions described in the spec. Please see the following for more details

### LibrarySystem
- A map stores current inventory, with `Item` as the key and available *unique id* list (can be emtpy) as the value
- Member data stores in another map, with *member id* as the key
- A list of current `Loan`s
- A date can be provided for testing only, otherwise it would be today's date
- A `ReentrantReadWriteLock.WriteLock` and a `ReentrantReadWriteLock.ReadLock` are used. 
  `ReadLock` are used when querying current stock and/or loan
  `WriteLock` are used only when changes are going to be made on current stock and/or loan
- Stream are used, including `parallelStream`, which may have a negative impact over small data set
#### Possible Change
- A map for *item Id* and `Item` can be passed to `LibrarySystem`. So `borrowItem` can first look up in the map using id to get an `Item`

## Tests
* Tests are under `test` folder, `resource` folder holds sample files to initialise current inventory, current loan and member data
* *book.csv* is reference data for all books, in store and lend out


