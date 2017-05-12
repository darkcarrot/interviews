from datetime import datetime


class Results:
    def __init__(self):
        self.price = None
        self.valuation_date = None
        self.model = None
        self.security = None

        self.float_cashflows = []
        self.fixed_cashflows = []

    def save(self, fname):
        f = None

        try:
            f = open(fname, 'w')
            f.write("NEWRESULTS\n")
            f.write("PRICE\t" + str(self.price) + "\n")
            f.write("VALUATIONDATE\t" + str(self.valuation_date) + "\n")
            f.write("MODEL\t" + self.model + "\n")
            f.write("SECURITY\t" + self.security + "\n")

            if (len(self.float_cashflows) > 0):
                f.write("SubTableBegin\tFloatLeg" + "\n")
                f.write("Date\tCashFlow" + "\n")
                for flow in self.float_cashflows:
                    f.write(str(flow[0]) + "\t" + str(flow[1]) + "\n")

            if (len(self.fixed_cashflows) > 0):
                f.write("SubTableBegin\tFixedLeg" + "\n")
                f.write("Date\tCashFlow" + "\n")
                for flow in self.fixed_cashflows:
                    f.write(str(flow[0]) + "\t" + str(flow[1]) + "\n")

        except:
            print "Failed to save Results object!"
        else:
            f.close()


class MyPricer:
    def __init__(self):
        """ Constructor.  
        
             Doesn't do anything in this instance, but you could imagine that it 
             would for a real pricer.
         """
        pass

    def calculateResults(self):
        """ Performs the pricing "calculation" and returns a populated Results object."""

        # This is a dummy program, so we're just going to populate the results manually
        # but you could imagine a real pricer would do this properly

        res = Results()
        res.price = 1.1234
        res.valuation_date = datetime.utcnow()
        res.model = "DBOPT-Deterministic"
        res.security = "PLAINSWAP"

        res.float_cashflows = [[20140301, 0.625], [20150301, 0.6516]]
        res.fixed_cashflows = [[20140301, 1.2], [20150301, 1.2]]

        return res


if __name__ == "__main__":
    fname = "results.sdos"
    pricer = MyPricer()
    results = pricer.calculateResults()
    results.save(fname)
