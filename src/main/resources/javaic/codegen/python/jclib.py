import math
import abc
import struct as _struct
import inspect
import numbers
import traceback
import random
import decimal
from _ast import Str



class java_lang_Object(metaclass=abc.ABCMeta):
    @classmethod
    def constructor_(cls, self):
        if not self: self = cls()
        return self
    
    def toString_(self):
        return jcl_String(self)
    
    def equals_O(self, c):
        return self == c    
    
class java_lang_Class(java_lang_Object):
    def __init__(self, name):
        self.name = name

    def newInstance_(self):
        return None

class java_util_Comparator(java_lang_Object):
    pass

class java_lang_Comparable(java_lang_Object):
    pass

class java_io_Serializable(java_lang_Object):
    pass
    
class jcl_String(java_lang_Object, str):
    def __init__(self, val):
        if type(val) == jcl_String:
            self.value = val.value
        elif type(val) == str:    
            self.value = val
        else:
            self.value = str(val)
        
    def startsWith(self, val):
        return self.value.startswith(val)
    
    def indexOf(self, val):
        return self.value.find(val)
    
    def __add__(self, val):
        return jcl_String(self.value + str(val))
    
    def compareTo_O(self, val):
        jcl_cast(val, jcl_String)
        if self.value < val.value:
            return -1
        elif self.value > val.value:
            return 1
        else:
            return 0
        
    def equals(self, val):
        return self.value == val.value

class CaseInsensitiveOrder:
    
    def compare_OO(self, o1, o2):
        return jcl_String(o1.upper()).compareTo_O(jcl_String(o2.upper()))

java_lang_String = jcl_String
java_lang_Comparable.register(java_lang_String)
jcl_String.CASE_INSENSITIVE_ORDER = CaseInsensitiveOrder()

class java_lang_StringBuffer(java_lang_Object):
    @classmethod
    def constructor_(cls, self):
        if not self: self = cls()
        self.value = ""
        return self   
    
    def append_R(self, s):
         self.value += s
         return self

    def append_C(self, s):
         self.value += s
         return self
     
    def append_O(self, obj):
         self.value += obj.toString_()
         return self
     
    def append_D(self, num):
        self.value += jcl_tostr(num)
        return self

    def append_L(self, num):
        self.value += jcl_tostr(num)
        return self
    
    def toString_(self):
        return jcl_String(self.value)

class java_lang_System:
    @staticmethod
    def arraycopy(src, srcInx, dest, destInx, length):
        dest[destInx:destInx + length] = src[srcInx:srcInx + length]
        #for i in range(0, length):
        #    dest[destInx + i] = src[srcInx + i]

    @staticmethod
    def currentTimeMillis_():
        return 0

class java_lang_Number(java_lang_Object):
    @classmethod
    def constructor_(cls, self):
        pass
    
#    def __str__(self):
#        return jcl_String(self.value)    
    
    def doubleValue_(self):
        return float(self.value)
    
    def intValue_(self):
        return int(self.value)

class java_lang_Float(java_lang_Number):
    
    @classmethod
    def constructor_D(cls, v, self):
        if not self: self = cls()
        super(__class__, cls).constructor_(self) 
        self.value = v
        return self
    
    
class java_lang_Double(java_lang_Number):
    
    @classmethod
    def constructor_D(cls, v, self):
        if not self: self = cls()
        super(__class__, cls).constructor_(self) 
        self.value = v
        return self

    @classmethod
    def constructor_I(cls, v, self):
        if not self: self = cls()
        super(__class__, cls).constructor_(self) 
        self.value = v
        return self
    
    @classmethod
    def constructor_R(cls, v, self):
        if not self: self = cls()
        super(__class__, cls).constructor_(self) 
        try:
            self.value = float(v)
        except:
            raise java_lang_Exception("Couldn't convert " + str(v) + " to float")
        return self

    
    @staticmethod
    def isNaN_D(val):
        return math.isnan(val)

    @staticmethod
    def isInfinite_D(val):
        return math.isinf(val)
        
    # --- The following are from http://symptotic.com/mj/double/double.py ---

    @staticmethod
    def doubleToRawLongBits_D(value):
        """
        @type  value: float
        @param value: a Python (double-precision) float value

        @rtype: long
        @return: the IEEE 754 bit representation (64 bits as a long integer)
                 of the given double-precision floating-point value.
        """
        # pack double into 64 bits, then unpack as long int
        return _struct.unpack('Q', _struct.pack('d', value))[0]

    @staticmethod
    def doubleToLongBits_D(value):
        return java_lang_Double.doubleToRawLongBits_D(value)
    
    @staticmethod
    def longBitsToDouble_L(bits):
        """
        @type  bits: long
        @param bits: the bit pattern in IEEE 754 layout

        @rtype:  float
        @return: the double-precision floating-point value corresponding
                 to the given bit pattern C{bits}.
        """
        return _struct.unpack('d', _struct.pack('Q', bits))[0]
    
    @classmethod
    def parseDouble_R(cls, val):
        return java_lang_Double.constructor_R(val, None).doubleValue_()

    def doubleValue(self):
        return self.value
    
    @classmethod
    def toString_D(cls, d):
        return jcl_tostr(d)
    
    @classmethod
    def valueOf_R(cls, s):
        return java_lang_Double.constructor_R(s, None)

java_lang_Double.NaN = float('nan')
java_lang_Double.POSITIVE_INFINITY = float("inf") # java_lang_Double.longBitsToDouble_L(0x7ff0000000000000)
java_lang_Double.MAX_VALUE         = java_lang_Double.longBitsToDouble_L(0x7fefFfffFfffFfff)
java_lang_Double.MIN_NORMAL        = java_lang_Double.longBitsToDouble_L(0x0010000000000000)
java_lang_Double.MIN_VALUE         = java_lang_Double.longBitsToDouble_L(0x0000000000000001)
java_lang_Double.NEGATIVE_ZERO     = java_lang_Double.longBitsToDouble_L(0x8000000000000000)
java_lang_Double.NEGATIVE_INFINITY = float("-inf") # java_lang_Double.longBitsToDouble_L(0xFff0000000000000)

class java_lang_Integer(java_lang_Number):
    @classmethod
    def constructor_I(cls, v, self):
        if not self: self = cls()
        super(__class__, cls).constructor_(self) 
        self.value = v
        return self
    
    def intValue_(self):
        return self.value
    
    @classmethod
    def toString_I(cls, i):
        return jcl_tostr(i)
    
    def longValue_(self):
        return self.value    
    
java_lang_Integer.register(int)

java_lang_Integer.MAX_VALUE = (2**31) - 1

class java_lang_Long(java_lang_Number):
    @classmethod
    def constructor_I(cls, v, self):
        if not self: self = cls()
        super(__class__, cls).constructor_(self) 
        self.value = v
        return self

    @classmethod
    def constructor_L(cls, v, self):
        if not self: self = cls()
        super(__class__, cls).constructor_(self) 
        self.value = v
        return self
    
    def compareTo_O(self, val):
        jcl_cast(val, java_lang_Long)
        return self.value - val.value
    
    def longValue_(self):
        return self.value
    
    def equals(self, val):
        return jcl_isinstance(val, java_lang_Long) and val.value == self.value
    
java_lang_Long.MAX_VALUE = (2 ** 63) - 1
java_lang_Long.MIN_VALUE = -(2 ** 63)
java_lang_Comparable.register(java_lang_Long)

class java_lang_Character(java_lang_Object):
    @classmethod
    def constructor_C(cls, v, self):
        if not self: self = cls()
        super(__class__, cls).constructor_(self) 
        self.value = v
        return self
    
    def compareTo_O(self, val):
        jcl_cast(val, java_lang_Character)
        return ord(self.value) - ord(val.value)
    
java_lang_Comparable.register(java_lang_Character)    

class java_lang_Math:
    @staticmethod
    def abs_D(num):
        return abs(num)
        
    @staticmethod
    def sqrt_D(num):
        return math.sqrt(num)

    @staticmethod
    def ceil_D(num):
        return math.ceil(num)
        
    @staticmethod
    def round_D(value):
      if value > java_lang_Long.MAX_VALUE:
        return java_lang_Long.MAX_VALUE
      elif value < java_lang_Long.MIN_VALUE:
        return java_lang_Long.MIN_VALUE
      elif math.isnan(value):
        return 0
      else:
        return round(value)
    
    @staticmethod
    def floor_D(value):
        if (value == java_lang_Double.POSITIVE_INFINITY or 
            value == java_lang_Double.NEGATIVE_INFINITY or
            value == 0 or
            math.isnan(value)):
            return value
        return math.floor(value)
    
    @staticmethod
    def exp_D(value):
        if math.isnan(value):
            return java_lang_Double.NaN
        elif value == java_lang_Double.POSITIVE_INFINITY:
            return java_lang_Double.POSITIVE_INFINITY
        elif value == java_lang_Double.NEGATIVE_INFINITY:
            return +0.0
        try:
            return math.exp(value)
        except OverflowError:
            return java_lang_Double.POSITIVE_INFINITY if value > 0 else java_lang_Double.NEGATIVE_INFINITY
    
    @staticmethod
    def log_D(value):
        if math.isnan(value) or value < 0:
            return java_lang_Double.NaN
        elif math.isinf(value):
            return java_lang_Double.POSITIVE_INFINITY
        elif value == 0:
            return java_lang_Double.NEGATIVE_INFINITY
        else:
            return math.log(value)
    
    @staticmethod
    def max_DD(num1, num2):
        return max(num1, num2)

    @staticmethod
    def max_II(num1, num2):
        return max(num1, num2)

    @staticmethod
    def min_DD(num1, num2):
        return min(num1, num2)
    
    @staticmethod
    def min_II(num1, num2):
        return min(num1, num2)

    @staticmethod
    def min_LL(num1, num2):
        return min(num1, num2)
    
    @staticmethod
    def sin_D(value):
        return math.sin(value)

    @staticmethod
    def pow_DD(base, exp):
        return math.pow(base, exp)

    @staticmethod
    def cos_D(value):
        return math.cos(value)
    
    @staticmethod
    def atan2_DD(y, x):
        return math.atan2(y, x)
    
    @staticmethod
    def random_():
        return random.random()
    
java_lang_Math.PI = math.pi    

class java_lang_Boolean:
    @classmethod
    def constructor_B(cls, v, self):
        if not self: self = cls()
        self.value = v
        return self
    
    def toString_(self):
        return jcl_String(self.value)
    
java_lang_Boolean.TRUE = java_lang_Boolean.constructor_B(True, None)

class java_util_Arrays:
    @staticmethod
    def equals(arr1, arr2):
        if arr1 == arr2:
            return True
        elif arr1 == None:
            return False
        elif len(arr1) != len(arr2):
            return False
        else:
            for i in range(0, len(arr1)):
                if arr1[i] != arr2[i]:
                    return False
                
        return True
    
    @staticmethod
    def binarySearch(arr, value):      

        lo = 0;
        hi = len(arr);
        while lo <= hi:
            mid = int((lo + hi) / 2)
            if arr[mid]  > value:
                hi = mid - 1;
            elif arr[mid] < value:
                lo = mid + 1
            else:
                return mid
        

        return -(lo + 1)
    
    @staticmethod
    def sort(arr):
        arr.sort()
        
def jcl_rshift(val, n): return (val % 0x100000000) >> n

def jcl_divint(n1, n2): return n1 // n2

def jcl_divfloat(n1, n2):
    #if math.isnan(n1) or math.isnan(n2):
    #    return NAN
    if n2 != 0:
        return n1 / n2
    else:
        return java_lang_Double.POSITIVE_INFINITY


def jcl_tostr(val):
    if type(val) == float:
        if math.isnan(val):
            return jcl_String("NaN")
        else:
            return jcl_String('%.15E' % val)
    else:
        return jcl_String(val)
    
def jcl_cast(val, typ):
    if val is None or jcl_isinstance(val, typ):
        return val
    else:
        raise java_lang_ClassCastException.constructor_R("Value not of type " + str(typ), None)

def jcl_isinstance(val, typ):
    return isinstance(val, typ)

class java_util_Random(java_lang_Object):
    @classmethod
    def constructor_L(cls, seed, self):
        if not self: self = cls()
        super(__class__, cls).constructor_(self) 
        # ignore seed
        return self

    @classmethod
    def constructor_(cls, self):
        if not self: self = cls()
        super(__class__, cls).constructor_(self) 
        return self
    
    def nextDouble_(self):
        return random.random()
    
    def setSeed_L(self, val):
        pass
    
    def nextGaussian_(self, x = 0, y = 0):
        return self.nextDouble_()
    

class junit_framework_TestCase:
    @classmethod
    def constructor_(cls, self):
        pass

    @classmethod
    def constructor_R(cls, self, name):
        pass
    
    def setUp_(self):
        pass
    
    def tearDown_(self):
        pass

    @staticmethod
    def assertEquals_ROO(msg, expected, actual):
        if isinstance(expected, jcl_String) and isinstance(actual, jcl_String):
            junit_framework_TestCase.assertEquals_RRR(msg, expected, actual)
        else:
            jcl_logEqualsObj(msg, expected, actual)
            result = (expected == actual or (expected != None and actual != None and expected.equals_O(actual)))
             
            junit_framework_TestCase.verify(result, msg)

    @staticmethod
    def assertEquals_OO(expected, actual):
        junit_framework_TestCase.assertEquals_ROO(None, expected, actual)

    @staticmethod
    def assertEquals_RRR(msg, expected, actual):
        jcl_logEqualsStr(msg, expected, actual)
        junit_framework_TestCase.verify(expected == actual, msg)

    @staticmethod
    def assertEquals_RR(expected, actual):
        junit_framework_TestCase.assertEquals_RRR(None, expected, actual)

    @staticmethod
    def assertEquals_RII(msg, expected, actual):
        jcl_logEquals('assertEquals', msg, expected, actual, 0)
        junit_framework_TestCase.verify(expected == actual)

    assertEquals_RSS = assertEquals_RII

    @staticmethod
    def assertEquals_II(expected, actual):
        junit_framework_TestCase.assertEquals_RII(None, expected, actual)

    assertEquals_SS = assertEquals_II

    @staticmethod
    def assertEquals_RBB(msg, expected, actual):
        jcl_logEquals('assertEquals', msg, expected, actual, 0)
        junit_framework_TestCase.verify(expected == actual)

    @staticmethod
    def assertEquals_BB(expected, actual):
        junit_framework_TestCase.assertEquals_RBB(None, expected, actual)

    @staticmethod
    def assertEquals_RLL(msg, expected, actual):
        junit_framework_TestCase.assertEquals_RII(msg, expected, actual)

    @staticmethod
    def assertEquals_LL(expected, actual):
        junit_framework_TestCase.assertEquals_RII(None, expected, actual)

    @staticmethod
    def assertEquals_RDDD(msg, expected, actual, delta):
        jcl_logEquals('assertEquals', msg, expected, actual, delta)
        junit_framework_TestCase.verifyNumEq(msg, expected, actual, delta)

    @staticmethod
    def assertEquals_DDD(expected, actual, delta):
        junit_framework_TestCase.assertEquals_RDDD(None, expected, actual, delta)
    
    
    @staticmethod
    def verifyNumEq(msg, n1, n2, delta):
        #jcl_logEquals('assertEquals', msg, n1, n2, delta)
        if (n1 == float("inf") or n2 == float("inf") or n1 == float("-inf") or n2 == float("-inf") or
          n1 == float("nan") or n2 == float("nan")):
          junit_framework_TestCase.verify(n1 == n2, msg=msg)
        else:            
          junit_framework_TestCase.verify(cond=abs(n1 - n2) <= delta, msg=msg)


    @staticmethod
    def assertTrue_RZ(msg, arg):
        jcl_logAssert('assertTrue', msg, arg)
        junit_framework_TestCase.verify(cond=arg, msg=msg)

    @staticmethod
    def assertTrue_Z(arg):
        junit_framework_TestCase.assertTrue_RZ(None, arg)
        
    @staticmethod
    def assertFalse_RZ(msg, arg):
        junit_framework_TestCase.assertTrue_RZ(msg, not arg)

    @staticmethod
    def assertFalse_Z(arg):
        junit_framework_TestCase.assertTrue_RZ(None, not arg)

    @staticmethod
    def assertNull_RO(msg, arg):
        junit_framework_TestCase.assertTrue_RZ(msg, arg is None)

    @staticmethod
    def assertNull_O(arg):
        jcl_logAssertNull(arg is None)
        junit_framework_TestCase.verify(arg is None)

    @staticmethod
    def assertNotNull_RO(msg, arg):
        junit_framework_TestCase.assertTrue_RZ(msg, arg is not None)

    @staticmethod
    def assertNotNull_O(arg):
        junit_framework_TestCase.assertTrue_RZ(None, arg is not None)
        
    @staticmethod
    def fail_(msg="Uh Oh"):
        junit_framework_TestCase.verify(cond=False, msg=msg)
        
    @staticmethod
    def verify(cond, msg="assertion failure"):
        if not cond:
            junit_framework_TestCase.reportError(msg)
            
    def _calltestmethod(self, method):
        try:
            if jcl_islogging:
                print("\t" + method.__name__ + "...")
            className = type(method.__self__).__name__.replace("_",".")
            if jcl_islogging:
                logfile_.write(className + "." + method.__name__.replace("_","") + "\n")

            self.setUp_()
            method()
            self.tearDown_()
        except:
            if jcl_islogging:
                traceback.print_exc()
            
    @staticmethod
    def reportError(msg):
        if not jcl_islogging:
            return
        frame = inspect.currentframe()
        # TODO: Need a more reliable approach to get the external caller's frame
        while inspect.getframeinfo(frame).filename.endswith('jclib.py'):
            frame = frame.f_back

        print(msg)
        while frame:
            frameinfo = inspect.getframeinfo(frame)
            print("\t" + frameinfo.filename + "(" + str(frameinfo.lineno) + ")")
            frame = frame.f_back

junit_framework_Assert = junit_framework_TestCase
org_junit_Assert = junit_framework_TestCase

def jcl_makeTest(cls):
    if 'constructor_R' in cls.__dict__:
        return cls.constructor_R('test', None)
    else:
        return cls.constructor_(None)

def jcl_runTest(cls):
    print("\n" + ("-"*40) + "\n" + cls.__name__ + ":")
    cls.jcl_runTest()

logfile_ = open(r'c:\temp\pyunit_trace.log','w')

def jcl_closeLogfile():
    if jcl_islogging:
        logfile_.close()
        
def jcl_logAssert(method, msg, value):
    if msg:
        msg = '"' + msg + '"'
    else:
        msg = 'null' 
    if jcl_islogging:        
        logfile_.write(method + ":" + msg + "`" + jcl_rendervalue(value) + "\n")
    
def jcl_logAssertNull(value):
    if jcl_islogging:
        logfile_.write("assertNull:" + jcl_rendervalue(value) + "\n")
 
def jcl_logEquals(method, msg, n1, n2, delta):
    if msg:
        msg = '"' + msg + '"'
    else:
        msg = 'null' 
    if delta == java_lang_Double.MIN_VALUE:
        delta = 0
    if jcl_islogging:        
        logfile_.write(method + ":" + msg + "`" + jcl_rendervalue(n1) +
                   "`" + jcl_rendervalue(n2) + "`" + jcl_rendervalue(delta) + "\n")

def jcl_logEqualsStr(msg, val1, val2):
    msg = 'null' if msg == None else '"' + msg + '"'
    val1 = 'null' if val1 == None else '"' + val1 + '"'
    val2 = 'null' if val2 == None else '"' + val2 + '"'
        
    if jcl_islogging:
        logfile_.write("assertEqualsStr:" + msg + "`" + val1 + "`" + val2 + "\n")

def jcl_logEqualsObj(msg, val1, val2):
    if msg:
        msg = '"' + msg + '"'
    else:
        msg = 'null'
    val1 = 'null' if val1 == None else 'notnull'
    val2 = 'null' if val2 == None else 'notnull'
         
    if jcl_islogging:
        logfile_.write("assertEqualsObj:" + msg + "`" + val1 + "`" + val2 + "\n")
        
def jcl_rendervalue(value):
    if type(value) == bool:
        if value:
            return 'true'
        else:
            return 'false'
    else:
        return jcl_tostr(value)    
    
class java_lang_Throwable(Exception):
    @classmethod
    def constructor_R(cls, msg, self):
        if not self: self = cls()
        self.__init__(msg) 
        return self

    def getMessage_(self):
        return self.args[0]
    
class java_lang_Exception(java_lang_Throwable):
    @classmethod
    def constructor_R(cls, msg, self):
        if not self: self = cls()
        super(__class__, cls).constructor_R(msg, self) 
        return self
        
class java_lang_RuntimeException(java_lang_Exception):
    @classmethod
    def constructor_R(cls, msg, self):
        if not self: self = cls()
        super(__class__, cls).constructor_R(msg, self) 
        return self

class java_lang_NullPointerException(IndexError, java_lang_Exception):
    @classmethod
    def constructor_R(cls, msg, self):
        if not self: self = cls()
        super(__class__, cls).constructor_R(msg, self) 
        return self

class java_lang_ArrayIndexOutOfBoundsException(IndexError, java_lang_Exception):
    @classmethod
    def constructor_R(cls, msg, self):
        if not self: self = cls()
        super(__class__, cls).constructor_R(msg, self) 
        return self


class java_lang_IllegalArgumentException(java_lang_Exception):
    @classmethod
    def constructor_R(cls, msg, self):
        if not self: self = cls()
        super(__class__, cls).constructor_R(msg, self) 
        return self

    @classmethod
    def constructor_(cls, self):
        if not self: self = cls()
        super(__class__, cls).constructor_R("IllegalArgumentException", self) 
        return self
        
class java_lang_ClassCastException(java_lang_Exception):
    @classmethod
    def constructor_R(cls, msg, self):
        if not self: self = cls()
        super(__class__, cls).constructor_R(msg, self) 
        return self
        
class java_lang_ArithmeticException(java_lang_Exception):
    @classmethod
    def constructor_R(cls, msg, self):
        if not self: self = cls()
        super(__class__, cls).constructor_R(msg, self) 
        return self
         
class java_lang_NumberFormatException(java_lang_Exception):
    @classmethod
    def constructor_R(cls, msg, self):
        if not self: self = cls()
        super(__class__, cls).constructor_R(msg, self) 
        return self
                  
         
         
def jcl_makeListArr(initValue, *dimens):
    if len(dimens) == 1:
        return [initValue] * dimens[0]
    elif len(dimens) == 2:
        
        return [[initValue] * dimens[1] for i in range(dimens[0])]
    elif len(dimens) == 3:
        return [[[initValue] * dimens[2] for i in range(dimens[1])] for i in range(dimens[0])]
    else:
        raise Exception("Unsupported number of dimensions: " + str(len(dimens)))
        
class java_util_HashMap:
    @classmethod
    def constructor_(cls, self):
        if not self: self = cls()
        self.map = {}
        return self
        
    def put_OO(self, k, v):
        original = None
        if k in self.map:
            original = self.map[k]
        self.map[k] = v
        return original

    def get_O(self, k):
        if k in self.map:
            return self.map[k]
        else:
            return None
    
    def containsKey_O(self, k):
        for akey in self.map.keys():
            if akey.equals_O(k):
                return True
    
        return False
    
    def containsValue_O(self, v):
        for aval in self.map.values():
            if aval.equals_O(v):
                return True
    
        return False
    
    def remove_O(self, k):
        del self.map[k]
        
    def keySet_(self):
        set = java_util_HashSet.constructor_(None)
        for k in self.map.keys():
            set.add_O(k)
            
        return set
    
    def values_(self):
        set = java_util_HashSet.constructor_(None)
        for v in self.map.values():
            set.add_O(v)
            
        return set
    
    def clear_(self):
        self.map.clear()

class java_util_TreeMap:
    @classmethod
    def constructor_(cls, self):
        if not self: self = cls()
        self.items = []    # contains ordered pairs [k, v]
        self.comparator = None
        self.doCompare = lambda a, b: a.compareTo_O(b)

        return self

    @classmethod
    def constructor_O20(cls, comp, self):
        if not self: self = cls()
        self.items = []    # contains ordered pairs [k, v]
        self.comparator = comp
        self.doCompare = lambda a, b: self.comparator.compare_OO(a, b)
        return self
                
    def put_OO(self, k, v):
        original = None
        i = 0
                    
        while i < len(self.items) and self.doCompare(self.items[i][0], k) < 0:
            i += 1
                
        if i < len(self.items):
            if self.doCompare(self.items[i][0], k) == 0:
                original = self.items[i][1]
                self.items[i][1] = v
            else:
                self.items.insert(i, [k, v])
        else:
            self.items.append([k, v])

        return original

    def get_O(self, k):
        i = 0
        while i < len(self.items) and self.doCompare(self.items[i][0], k) != 0:
            i += 1
            
        if i < len(self.items):
            return self.items[i][1]
        
        return None
    
    def containsKey_O(self, k):
        i = 0
        while i < len(self.items) and self.items[i][0].compareTo_O(k) != 0:
            i += 1
            
        return i < len(self.items)
    
    def remove_O(self, k):
        i = 0
        while i < len(self.items) and self.items[i][0].compareTo_O(k) != 0:
            i += 1
            
        if i < len(self.items):
            del self.items[i]
        
        
    def keySet_(self):
        set = java_util_HashSet.constructor_(None)
        for itm in self.items:
            set.add_O(itm[0])
            
        return set
    
    def clear_(self):
        self.items.clear()
    
    def values_(self):
        set = java_util_HashSet.constructor_(None)
        for itm in self.items:
            set.add_O(itm[1])
            
        return set
    
    def firstKey_(self):
        if len(self.items) > 0:
            return self.items[0][0]
        
        return None
    
    def lastKey_(self):
        if len(self.items) > 0:
            return self.items[-1][0]
        
        return None
    
    def comparator_(self):
        return self.comparator
        
class java_util_List(java_lang_Object):
    pass        
        
class java_util_ArrayList(java_util_List):
    
    @classmethod
    def constructor_(cls, self):
        if not self: self = cls()
        self.list = []
        return self
    
    def add_O(self, v): 
        self.list.append(v)
        
    def remove_O(self, v):
        self.list.remove(v)
        
    def toArray_(self):
        return self.list[:]
    
    def size_(self):
        return len(self.list)
    
    def get_I(self, pos):
        return self.list[pos]
    
    def clear_(self):
        self.list = []
        
    def iterator_(self):
        return ListIterator(self)
    
    def contains_O(self, v):
        for val in self.list:
            if val.equals_O(v):
                return True
            
        return False
        
java_util_Vector = java_util_ArrayList

class java_util_HashSet(java_util_ArrayList):
    @classmethod
    def constructor_(cls, self):
        if not self: self = cls()
        self.list = []
        return self
    
    def equals_O(self, hs):
        return set(self.list) == set(hs.list)

class ListIterator:
    def __init__(self, arrayList):
        self.arrayList = arrayList
        self.pos = 0
        
    def hasNext_(self):
        return self.pos < self.arrayList.size_()
    
    def next_(self):
        item = self.arrayList.get_I(self.pos)
        self.pos += 1
        return item        
    
class java_math_BigDecimal:
    def __init__(self, dec=None):
        self.value = dec
    
    @classmethod
    def constructor_I(cls, num, self):
        if not self: self = cls()
        self.value = decimal.Decimal(num)
        return self

    @classmethod
    def constructor_R(cls, num, self):
        if not self: self = cls()
        try:
            self.value = decimal.Decimal(num)
        except decimal.InvalidOperation:
            raise java_lang_NumberFormatException()
        return self
    
    @classmethod
    def constructor_D(cls, num, self):
        if not self: self = cls()
        self.value = decimal.Decimal(num)
        return self
    
    def equals_O(self, bigdec):
        return self.value == bigdec.value
    
    def add_O20(self, bigdec):
        return java_math_BigDecimal(self.value + bigdec.value)
    
    def subtract_O20(self, bigdec):
        return java_math_BigDecimal(self.value - bigdec.value)

    def multiply_O20(self, bigdec):
        return java_math_BigDecimal(self.value * bigdec.value)

    def divide_O20(self, bigdec):
        return java_math_BigDecimal(self.value / bigdec.value)

    def divide_OII20(self, bigdec, scale, round):
        return java_math_BigDecimal(self.value / bigdec.value)
    
    def abs_(self):
        return java_math_BigDecimal(self.value.copy_abs())

    def negate_(self):
        return java_math_BigDecimal(-self.value)
    
    def doubleValue_(self):
        return float(self.value)
    
    def max_O20(self, bigdec):
        return java_math_BigDecimal(self.value.max(bigdec.value))
    
    def hashCode_(self):
        return self.value.__hash__()
    
    def compareTo_O20(self, bigdec):
        return int(self.value.compare(bigdec.value))
    
    def toString_(self):
        return jcl_String(str(self.value))
    
java_math_BigDecimal.ROUND_HALF_UP = 0
    
    
class java_text_NumberFormat:
    
    def __init__(self):
        pass
    
    @classmethod
    def getPercentInstance_(cls):
        return java_text_NumberFormat()
    
    def format_D(self, val):
        return jcl_tostr(val)    
    
jcl_islogging = True

def disable_logging():
    global jcl_islogging
    jcl_islogging = False