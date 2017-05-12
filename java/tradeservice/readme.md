A simple webservice which accepts Trade object from a post message.
_TradeServiceTest_ can be used for a test on local box.

_TradeCache_ class handles incoming trade and stores the maximum price, average price (in terms of total price and number of trade), and numbers of trades associated with flags for each symbol.
ReadLock and WriteLock are used for accessing each set of data.
 
**TODO**:
- logging
- comments
- further unit test on _TradeCache_
- integration tests

ZC