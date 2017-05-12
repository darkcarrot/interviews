import sqlite3

DB_NAME = 'TBD'  # To be defined

# create a connection
conn = sqlite3.connect(DB_NAME)
# create a cursor
c = conn.cursor()
try:
    # Create tables
    # TradeTable
    c.execute('''CREATE TABLE TradeTable( TradeID int, PRIMARY KEY (TradeID) )''')

    # TradeTable
    # Why both tables have the same name?
    c.execute('''CREATE TABLE TradeTable2( TradeID int,
                                            SecurityType varchar(255),
                                            FOREIGN KEY (TradeID) REFERENCES TradeTable(TradeID) )''')
    # PriceTable
    c.execute('''CREATE TABLE PriceTable( TradeID int,
                                           NetPV real,
                                           ScenarioCode int,
                                           FOREIGN KEY (TradeID) REFERENCES TradeTable(TradeID) )''')
except sqlite3.Error as e:
    print 'Error occured: {0}'.format(e)
else:
    # commit the changes
    conn.commit()
finally:
    # close the connection
    conn.close()
