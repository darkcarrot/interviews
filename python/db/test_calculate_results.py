import os
import unittest

from calculate_results import MyPricer, Results


class TestMyPricer(unittest.TestCase):
    MAP = {'PRICE': ('price', float),
           'MODEL': ('model', str),
           'SECURITY': ('security', str)}

    TEST_FILE = 'test.sdos'

    def setUp(self):
        self.pricer = MyPricer()

    def tearDown(self):
        if os.path.exists(self.TEST_FILE):
            os.remove(self.TEST_FILE)

    def testCalculateResults(self):
        """test MyPricer.calculateResults()"""
        base_res = self.readExample()
        cal_res = self.pricer.calculateResults()
        self.compareResult(base_res, cal_res)

    def testResultsSave(self):
        """test Results.save()"""
        base_res = self.readExample()
        cal_res = self.pricer.calculateResults()
        try:
            cal_res.save(self.TEST_FILE)
        except:
            print "Failed to save Results object!"
        else:
            test_res = self.readExample(self.TEST_FILE)
            self.compareResult(base_res, test_res)

    def compareResult(self, base_res, cal_res):
        """compare require fields"""
        self.assertEqual(base_res.price, cal_res.price, 'price not equal')
        self.assertEqual(base_res.model, cal_res.model, 'model not equal')  # Determinisitc: typo?
        self.assertEqual(base_res.security, cal_res.security, 'security not equal')
        self.assertEqual(base_res.float_cashflows, cal_res.float_cashflows, 'float cashflows not equal')
        self.assertEqual(base_res.fixed_cashflows, cal_res.fixed_cashflows, 'fixed cashflows not equal')

    def readExample(self, file_name='example.sdos'):
        """
        read the example file and construct an object
        Avoid file to file compare
        """
        res = Results()
        with open(file_name, 'rb') as f:
            raw_base = f.readlines()
        if raw_base:
            raw_base = [item.rstrip().split('\t') for item in raw_base if '\t' in item]
            for i, k in raw_base:
                if i in self.MAP:
                    field, filed_type = self.MAP[i]
                    setattr(res, field, filed_type(k))
                elif i == 'SubTableBegin':
                    cur_index = raw_base.index([i, k])
                    date_cashflow = self.getCashflow(raw_base[cur_index + 1:])
                    if k == 'FloatLeg':
                        res.float_cashflows = date_cashflow
                    elif k == 'FixedLeg':
                        res.fixed_cashflows = date_cashflow
                    else:
                        pass  # why am I here
                else:
                    pass
        return res

    def getCashflow(self, input_list):
        """
        Get the cash flow section from the list
        Assume the example file would always have 'Date Cashflow' line,
        and they are in standard format as expected, otherwise more regex/conversion required
        """
        result = []
        inverse = True if ['CashFlow', 'Date', ] == input_list[0] else False
        for i, k in input_list[1:]:
            if i not in self.MAP.keys() + ['SubTableBegin', ]:
                result.append([int(k), float(i)] if inverse else [int(i), float(k)])
            else:
                break
        return result


if __name__ == "__main__":
    # import sys;sys.argv = ['', 'Test.test']
    unittest.main()
