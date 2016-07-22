#ifndef _CPP_LIB_H
#define _CPP_LIB_H

#include <cstdint>
#include <string>
#include <vector>
#include <map>
#include <set>
#include <limits>
#include <iostream>
#include <fstream>
#include <functional>
#include <algorithm>

extern std::ofstream logstrm;

// forward declarations
class java_lang_String;
class java_io_PrintWriter;
class java_lang_Object;
class java_lang_Class;


class java_lang_Object {
public:
	virtual java_lang_String* toString();
	virtual int32_t hashCode() { return 0;  }
	virtual bool equals(java_lang_Object *that) {
		return this == that;
	}
	virtual java_lang_Class* getClass() { return NULL;  }
};

template <class T>
class java_util_Comparator : public java_lang_Object {
public:
	virtual int32_t compare(T o1_, T o2_) = 0;
};

class java_lang_Comparable: public virtual java_lang_Object {
public:
	virtual int compareTo(java_lang_Object* c) = 0;
};


class java_lang_Throwable : public std::exception, public java_lang_Object {
	java_lang_String* msg;
public:
	static java_lang_Class* class_() { return NULL; }
	java_lang_Throwable();
	java_lang_Throwable(java_lang_String *msg);
	virtual const char* what() const;
	java_lang_String* getMessage() { return msg; }
	void printStackTrace(java_io_PrintWriter* out);
};

class java_lang_Exception : public java_lang_Throwable {


public:
	java_lang_Exception()  { }
	java_lang_Exception(java_lang_String *msg) : java_lang_Throwable(msg) { }

};

#define STD_EXCEPTION(EXC_NAME) class EXC_NAME: public java_lang_Exception { \
public: \
EXC_NAME(java_lang_String *msg) : java_lang_Exception(msg) { } \
EXC_NAME() { } \
}

STD_EXCEPTION(java_lang_ClassCastException);
STD_EXCEPTION(java_lang_IllegalArgumentException);
STD_EXCEPTION(java_lang_ArithmeticException);
STD_EXCEPTION(java_lang_RuntimeException);
STD_EXCEPTION(java_lang_IllegalStateException);
STD_EXCEPTION(java_lang_ArrayIndexOutOfBoundsException);
STD_EXCEPTION(java_lang_NumberFormatException);
STD_EXCEPTION(java_lang_NoSuchMethodException);
STD_EXCEPTION(junit_framework_AssertionFailedError);
// -------------------- Array Support ----------------------

#define AT(inx) at(inx,__FILE__,__LINE__)


template<class T>
struct Array : public java_lang_Object {

private:
	std::vector<T> data_;
	const size_t size_;
public:
	Array(int size, T defVal): data_(std::vector<T>(size, defVal)), size_(size) {	}
	Array(const std::vector<T>& v): data_(v), size_(v.size()) { }

	size_t length() {
		return size_;
	}

	// T& operator[]
	T& at(const int inx, char *file, int line) {
		if (inx < 0 || inx >= size_)
			throw new java_lang_ArrayIndexOutOfBoundsException((new java_lang_String("Index out of bounds: "))
			->concat(jcl_tostr(inx))
			->concat(new java_lang_String(" at "))
			->concat(new java_lang_String(file))
			->concat(new java_lang_String("("))
			->concat(jcl_tostr(line))
			->concat(new java_lang_String(")\n"))); 
		return data_[inx];
	}
	void sort() {
		std::sort(data_.begin(), data_.end());
	}
};



// Single dimension array
template<class T>
Array<T>* createArray(T defVal, int d1size) {
	Array<T>* arr = new Array<T>(d1size, defVal);
	return arr;
}

// Two dimension array
template<class T>
Array<Array<T>*>* createArray(T defVal, int d1size, int d2size) {
	Array<Array<T>*>* arr = new Array<Array<T>*>(d1size, NULL);
	for (int i = 0; i < d1size; ++i) {
		arr->AT(i) = new Array<T>(d2size, defVal);
	}
	return arr;
}

class java_lang_Class: public java_lang_Object {
    std::string name_;
public:
	java_lang_Class(std::string name): name_(name) {	}

	java_lang_Object *newInstance() {
		return NULL;
	}

	void getDeclaredMethod(java_lang_String* s, Array<java_lang_Class*> *a) { }
	
};


class java_lang_String : public java_lang_Comparable {
public:
	std::string str;
	static java_util_Comparator<java_lang_Object*>* CASE_INSENSITIVE_ORDER_;
public:
	java_lang_String(const char* ch) : str(ch) { }

	java_lang_String(Array<char>* chars, int32_t, int32_t);

	java_lang_String(const std::string str) : str(str) { }

	long length() {
		return str.size();
	}

	char charAt(long pos) {
		return str.at(pos);
	}

	java_lang_String* concat(java_lang_String* value) {
		return new java_lang_String(str + value->str);
	}

	java_lang_String* substring(int32_t start, int32_t end) {
		return new java_lang_String(str.substr(start, end - start));
	}

	java_lang_String* substring(int32_t start) {
		return new java_lang_String(str.substr(start));
	}

	java_lang_String* toString() {
		return this;
	}

	bool equals(java_lang_Object *value) {
		if (!value)
			return false;

		java_lang_String* that = value->toString();
		return (str == that->str);
	}

	bool startsWith(java_lang_String *value) {
		return this->str.length() >= value->str.length() &&
			this->str.substr(0, value->str.length()) == value->str;
	}

	int32_t indexOf(java_lang_String *value) {
		return this->str.find(value->str);
	}

	int32_t indexOf(java_lang_String *value, int32_t startPos) {
		return this->str.find(value->str, startPos);
	}

	int compareTo(java_lang_Object* c) {
		java_lang_String *lng = dynamic_cast<java_lang_String*>(c);
		if (lng) {
			return str.compare(lng->str);
		}
		else {
			throw new java_lang_ClassCastException();

		}
	}
};

class java_lang_Character: public virtual java_lang_Object, public java_lang_Comparable {
	char ch_;
public:
	java_lang_Character(char c) : ch_(c) { }
	static bool isDigit(char c) { return isdigit(c) != 0;  }
	static char forDigit(int32_t digit, int32_t radix) { return digit + '0';  }
	static int digit(char c, int32_t radix) { return isDigit(c) ? c - 48 : -1;  }
	bool equals(java_lang_Object *value) {
		java_lang_Character *ch = dynamic_cast<java_lang_Character*>(value);
		return ch && ch->ch_ == this->ch_;
	}

	int compareTo(java_lang_Object* c) {
		java_lang_Character *lng = dynamic_cast<java_lang_Character*>(c);
		if (lng) {
			return ch_ - (int32_t)lng->ch_;
		}
		else {
			throw new java_lang_ClassCastException();

		}
	}
};

java_lang_String* jcl_tostr(int64_t value);
java_lang_String* jcl_tostr(int32_t value);
java_lang_String* jcl_tostr(int16_t value);
java_lang_String* jcl_tostr(int8_t value);
java_lang_String* jcl_tostr(bool value);
java_lang_String* jcl_tostr(double value);
java_lang_String* jcl_tostr(char value);

uint64_t jcl_tounsigned(int8_t num);

uint64_t jcl_tounsigned(int16_t num);

uint64_t jcl_tounsigned(int32_t num);

uint64_t jcl_tounsigned(int64_t num);


class java_lang_StringBuffer {
	std::string value;
public:
	java_lang_StringBuffer() { }
	java_lang_StringBuffer(int32_t) { }

	java_lang_StringBuffer *append(java_lang_String* str) {
		value += str->str;
		return this;
	}

	java_lang_StringBuffer *append(java_lang_Object* obj) {
		value += obj->toString()->str;
		return this;
	}

	java_lang_StringBuffer *append(char val) {
		value += val;
		return this;
	}

	java_lang_StringBuffer *append(int32_t val) {
		value += jcl_tostr(val)->str;
		return this;
	}

	java_lang_StringBuffer *append(int64_t val) {
		value += jcl_tostr(val)->str;
		return this;
	}

	java_lang_StringBuffer *append(double val) {
		value += jcl_tostr(val)->str;
		return this;
	}

	char charAt(int32_t loc) {
		return value.at(loc);
	}

	int32_t length() {
		return value.length();
	}

	void setCharAt(int32_t pos, char c) { value.at(pos) = c; }

	void insert(int32_t pos, char c) { value.insert(pos, 1, c); }

	void append(Array<char>*, int32_t start, int32_t end);

	java_lang_String* toString() {
		return new java_lang_String(value);
	}
};

typedef java_lang_StringBuffer java_lang_StringBuilder;



template<class Target, class Source>
bool instanceOf(Source src) {
	return dynamic_cast<Target>(src) != NULL;
}




class java_io_PrintStream {
    std::ostream& strm;
public:
    java_io_PrintStream(std::ostream &strm) : strm(strm) { }
    void println(java_lang_String *msg) {
        this->strm << msg->str << std::endl;
    }
};

class java_io_PrintWriter {
    java_io_PrintStream *out;
public:
    java_io_PrintWriter(java_io_PrintStream *out, bool flush) : out(out) { }
    void print(java_lang_String *msg) {
        out->println(msg);
    }
    void flush() { }
};

class java_lang_System {
public:
    static java_io_PrintStream* out_;
    static java_io_PrintStream* err_; 

	template<class T>
	static void arraycopy(Array<T>* src, int32_t srcPos, Array<T>* dest, int32_t destPos, int32_t length) {
		for (int i = 0; i < length; ++i) {
			dest->AT(i + destPos) = src->AT(i + srcPos);
		}
	}

	static int64_t currentTimeMillis() { return 0;  }
};



class java_io_Serializable: virtual public java_lang_Object { };

template<class Target, class Source>
Target jcl_dynamicCast(Source src) {
	if (src == NULL)
		return NULL;
    Target t = dynamic_cast<Target>(src);
    if (t == NULL)
        throw new java_lang_ClassCastException();

    return t;
}

class java_lang_Number : public virtual java_lang_Object {
public:
	virtual double doubleValue() = 0;
};

class java_lang_Double : public java_lang_Number {
	double value;
public:
    static double NaN_;
    static double MAX_VALUE_, MIN_VALUE_, POSITIVE_INFINITY_, NEGATIVE_INFINITY_;

	java_lang_Double(double val) : value(val) { }

	java_lang_Double(int32_t val) : value(val) { }

	java_lang_Double(int64_t val) : value(val) { }

	java_lang_Double(java_lang_String *val) { 
		size_t idx = 0;
		try {
			value = std::stod(val->str, &idx);
		}
		catch (std::invalid_argument) {
			throw new java_lang_Exception(new java_lang_String(val->str + " cannot convert to double"));
		}
		if (idx != val->str.length())
			throw new java_lang_Exception(new java_lang_String(val->str + " cannot convert complete to double"));
	}



    static uint64_t doubleToRawLongBits(double val) {
        uint64_t bits;
        memcpy(&bits, &val, sizeof bits);
        return bits;
    }

    static uint64_t doubleToLongBits(double val) {
        uint64_t bits;
        memcpy(&bits, &val, sizeof bits);
        return bits;
    }

	static double longBitsToDouble(uint64_t val) {
		double d;
		memcpy(&d, &val, sizeof(d));
		return d;
	}

    static bool isNaN(double val) {
        return std::isnan(val);
    }

    static bool isInfinite(double val) {
        return std::isinf(val);
    }

	double doubleValue() {
		return value;
	}

	static java_lang_Double *valueOf(java_lang_String *s) {
		return new java_lang_Double(s);
	}

	static java_lang_String* toString(double d) {
		return jcl_tostr(d);
	}

	static double parseDouble(java_lang_String *s) {
		return (new java_lang_Double(s))->doubleValue();
	}
};

typedef java_lang_Double java_lang_Float;

class java_lang_Integer: public java_lang_Number {
	int32_t value;
public:
	static int32_t MAX_VALUE_, MIN_VALUE_;

	java_lang_Integer(int32_t val) : value(val) { }

	double doubleValue() { return value;  }

	int32_t intValue() { return value;  }

	int64_t longValue() { return value;  }

	static java_lang_String* toString(int32_t,int32_t = 10);


	static int32_t parseInt(java_lang_String* s, int32_t);

	static int32_t parseInt(java_lang_String* s) {
		return parseInt(s, 10);
	}
};

class java_lang_Math {
public:
	static double PI_;

	static double ceil(double value) {
		return std::ceil(value);
	}

    static double sqrt(double value) {
        return std::sqrt(value);
    }

    static double abs(double value) {
        return std::abs(value);
    }

    static int64_t round(double value);

    static double exp(double value) {
        return std::exp(value);
    }

    static double floor(double value) {
        return std::floor(value);
    }

    static double log(double value) {
        return std::log(value);
    }

	static double sin(double value) {
		return std::sin(value);
	}

	static double cos(double value) {
		return std::cos(value);
	}

	static double atan2(double x, double y) {
		return std::atan2(x, y);
	}

	static double max(double num1, double num2) {
        return (num1 > num2) ? num1 : num2;
    }

    static double min(double num1, double num2) {
        return (num1 < num2) ? num1 : num2;
    }

	static double pow(double base, double exp) {
		return std::pow(base, exp);
	}

	static double random() {
		return std::rand() / RAND_MAX;
	}
};

class java_util_Random: public java_lang_Object {
public:
	java_util_Random() { }
	java_util_Random(int64_t seed) { setSeed(seed);  }
	void setSeed(int64_t value) { }

	double nextDouble() {
		return std::rand() * 1.0 / (RAND_MAX + 1);
	}

	double nextGaussian(double x, double y) { return nextDouble();  }
	double nextGaussian() { return nextDouble(); }
};

class java_lang_Long : public java_lang_Number, public virtual java_lang_Comparable {
	int64_t value_;
public:
    static int64_t MIN_VALUE_, MAX_VALUE_;

	java_lang_Long(int64_t value): value_(value) { }

	double doubleValue() { return value_; }

	int64_t longValue() { return value_; }

	static java_lang_String* toString(int64_t, int32_t);

	static int64_t parseLong(java_lang_String*, int32_t);

	bool equals(java_lang_Object *value) {
		java_lang_Long *lng = dynamic_cast<java_lang_Long*>(value);
		return lng && lng->value_ == this->value_;
	}

	int32_t compareTo(java_lang_Object* c) {
		java_lang_Long *lng = dynamic_cast<java_lang_Long*>(c);
		if (lng) {
			int64_t result = this->value_ - lng->value_;
			return result > 0 ? 1 : 
				(result < 0 ? -1 : 0);
		}
		else {
			throw new java_lang_ClassCastException();

		}
	}

};

class java_lang_Boolean : public java_lang_Object {
	bool value;
public:
	static java_lang_Boolean* TRUE_;

	java_lang_Boolean(bool val) : value(val) { }
};

class java_util_Arrays {
public:
	template <class T>
	static int32_t binarySearch(Array<T>* arr, T value) {
		int32_t lo, hi, mid;

		lo = 0;
		hi = arr->length();
		while (lo <= hi) {
			mid = (lo + hi) / 2;
			if (arr->AT(mid) > value)
				hi = mid - 1;
			else if (arr->AT(mid) < value)
				lo = mid + 1;
			else
				return mid;
		}

		return -(lo + 1);
	}

	template <class T>
	static bool equals(Array<T>* arr1, Array<T>* arr2) {
		if (arr1 == NULL || arr2 == NULL)
			return false;
		if (arr1->length() != arr2->length())
			return false;

		for (int32_t i = 0; i < arr1->length(); ++i) {
			if (arr1->AT(i) != arr2->AT(i))
				return false;
		}

		return true;
	}

	template<class T>
	static void sort(Array<T>* arr) {
		arr->sort();
	}
};


class junit_framework_Assert : public java_lang_Object {
public:
	static void assertEquals(double expected, double actual, double tolerance, std::string file, int line);

	static void assertEquals(java_lang_String* msg, double expected, double actual, double tolerance, std::string file, int line);

	static void assertEquals(java_lang_String* msg, int64_t expected, int64_t actual, std::string file, int line);

	static void assertEquals(int64_t expected, int64_t actual, std::string file, int line);

	static void assertEquals(java_lang_String* expected, java_lang_String* actual, std::string file, int line);

	static void assertEquals(java_lang_String* msg, java_lang_String* expected, java_lang_String* actual, std::string file, int line);

	static void assertEquals(java_lang_Object* expected, java_lang_Object* actual, std::string file, int line);

	static void assertEquals(java_lang_String* msg, java_lang_Object* expected, java_lang_Object* actual, std::string file, int line);

	static void assertTrue(bool cond, std::string file, int line);

	static void assertTrue(java_lang_String* msg, bool cond, std::string file, int line);

	static void assertFalse(bool cond, std::string file, int line);

	static void assertFalse(java_lang_String* msg, bool cond, std::string file, int line);

	static void assertNotNull(void* obj, std::string file, int line);

	static void assertNotNull(java_lang_String* msg, void* obj, std::string file, int line);

	static void assertNull(void* obj, std::string file, int line);

	static void assertNull(java_lang_String *msg, void* obj, std::string file, int line);

	static void fail(java_lang_String* msg, std::string file, int line);

	static void fail(std::string file, int line);

	static void reportError(std::string msg, std::string file, int line);

};

class junit_framework_TestCase : public junit_framework_Assert {
public:
    junit_framework_TestCase() { }

    junit_framework_TestCase(java_lang_String* name) { }

	void setUp() { }

	void tearDown() { }

	void runTest() { }

};


class java_text_NumberFormat {
public:
	static java_text_NumberFormat* getPercentInstance() {
		return new java_text_NumberFormat();
	}

	java_lang_String* format(double value) {
		return jcl_tostr(value);
	}
};



template <class T>
class java_util_Iterator {
public:
	virtual bool hasNext() = 0;
	virtual T next() = 0;
};

template <class T>
class java_util_Iterable {
public:
	virtual java_util_Iterator<T>* iterator() = 0;
};

template <class T>
class ListIterator: public java_util_Iterator<T> {
	std::vector<T> items;
	int32_t pos = 0;
public:
	ListIterator(std::vector<T> it) : items(it) { }
	bool hasNext() {
		return pos < items.size();
	}

	T next() {
		return items.at(pos++);
	}
};

template <class T>
class java_util_Collection: public virtual java_util_Iterable<T> {
public:
	virtual void add(T item) = 0;
	virtual int32_t size() = 0;
	virtual Array<java_lang_Object*>* toArray() = 0;
	virtual bool contains(T value) = 0;
};

template <class T>
class java_util_List: public virtual java_lang_Object, public virtual java_util_Collection<T> {
public:
	virtual void add(T item) = 0;
	virtual T remove(T item) = 0;
	virtual int32_t size() = 0;
	virtual T get(int32_t index) = 0;
	virtual void clear() = 0;
	virtual Array<java_lang_Object*>* toArray() = 0;
	virtual java_util_Iterator<T> *iterator() = 0;
};

template <class T>
class java_util_Set : public virtual java_util_Collection<T> {

};

template <class T>
class java_util_ArrayList : public virtual java_lang_Object, public virtual java_util_List<T> {
protected:
	std::vector<T> items;
public:
	virtual void add(T item) {
		items.push_back(item);
	}

	virtual T remove(T item) {
		
		std::vector<T>::iterator it = items.begin();
		while (it != items.end() && !(*it)->equals(item))
			++it;

		T removedItem = NULL;

		if (it != items.end()) {
			item = *it;
			items.erase(it);
		}

		return removedItem;
			
	}

	virtual int32_t size() {
		return items.size();
	}

	virtual T get(int32_t index) {
		return items.at(index);
	}

	virtual void clear() {
		items.clear();
	}

	virtual Array<java_lang_Object*>* toArray() {
		auto arr = new Array<java_lang_Object*>(items.size(), NULL);
		for (int32_t i = 0; i < items.size(); ++i) {
			arr->AT(i) = jcl_dynamicCast<java_lang_Object*>(items.at(i));
		}
		return arr;
	}

	virtual java_util_Iterator<T> *iterator() {
		return new ListIterator<T>(items);
	}

	bool contains(T item) {
		std::vector<T>::iterator it = items.begin();
		while (it != items.end() && !(*it)->equals(item))
			++it;

		return it != items.end();

	}
};

template <class T>
class java_util_Vector : public java_util_ArrayList < T > {

};



template<class K, class V> 
class java_util_Map : public virtual java_lang_Object {
public:
	virtual V put(K key, V value) = 0;
	virtual V get(K key) = 0;
	virtual bool containsKey(K key) = 0;
	virtual bool containsValue(V value) = 0;
	virtual V remove(K key) = 0;
	virtual void clear() = 0;
	virtual java_util_Set<K>* keySet() = 0;
	virtual java_util_Collection<V>* values() = 0;
};

template<class K, class V>
class java_util_HashMap : public java_util_Map<K, V>, public virtual java_lang_Object {
protected:
	std::map<K, V> items;
public:
	V put(K key, V value) {
		V origValue = NULL;
		std::map<K, V>::iterator it = items.begin();
		while (it != items.end() && !it->first->equals(key))
			++it;
		if (it != items.end()) {
			origValue = it->second;
			it->second = value;
		}
		else {
			items[key] = value;
		}
		return origValue;
	}

	V get(K key) {
		V value = NULL;
		std::map<K, V>::iterator it = items.begin();
		while (it != items.end() && !it->first->equals(key))
			++it;
		if (it != items.end())
			value = it->second;
		return value;
	}

	bool containsKey(K key) {
		std::map<K, V>::iterator it = items.begin();
		while (it != items.end() && !it->first->equals(key))
			++it;

		return it != items.end();
	}

	bool containsValue(V value) {
		std::map<K, V>::iterator it = items.begin();
		while (it != items.end() && !it->second->equals(value))
			++it;

		return it != items.end();
	}

	V remove(K key) {
		std::map<K, V>::iterator it = items.begin();
		while (it != items.end() && !it->first->equals(key))
			++it;

		V item = NULL;
		if (it != items.end()) {
			item = it->second;
			items.erase(it);
		}

		return item;
	}

	void clear() {
		items.clear();
	}

	java_util_Set<K>* keySet() {
		java_util_Set<K>* set = new java_util_HashSet<K>();
		std::map<K, V>::iterator it = items.begin();
		while (it != items.end()) {
			set->add(it->first);
			++it;
		}
		return set;

	}

	java_util_Collection<V>* values() {
		java_util_Collection<V>* vec = new java_util_ArrayList<V>();
		std::map<K, V>::iterator it = items.begin();
		while (it != items.end()) {
			vec->add(it->second);
			++it;
		}
		return vec;
	}

};

template <class K, class V>
class Pair {
public:
	K first;
	V second;
	Pair(K k, V v) : first(k), second(v) { }
};

template <class K, class V>
class java_util_TreeMap : public java_util_Map < K, V > {
	java_util_Comparator<K>* comparator_;
	std::function<int(java_lang_Object*, java_lang_Object*)> doCompare_;
public:
	java_util_TreeMap() : comparator_(NULL) { 
		doCompare_ = [](java_lang_Object*o1, java_lang_Object*o2) {
			java_lang_Comparable* c = jcl_dynamicCast<java_lang_Comparable*>(o1);
			return c->compareTo(o2);
		};
	}
	java_util_TreeMap(java_util_Comparator<K>* comparator) : comparator_(comparator) { 
		doCompare_ = [this](java_lang_Object*o1, java_lang_Object *o2) { return this->comparator_->compare(o1, o2);  };
	}

protected:
	std::vector<Pair<K,V>> items;
public:
	V put(K key, V value) {
		V origValue = NULL;



		std::vector<Pair<K, V>>::iterator it = items.begin();
		while (it != items.end() && doCompare_(it->first, key) < 0)
			++it;
		if (it != items.end()) {
			if (doCompare_(it->first, key) == 0) {
				origValue = it->second;
				it->second = value;
			}
			else {
				items.insert(it, Pair<K, V>(key, value));
			}
		}
		else {
			items.insert(it, Pair<K, V>(key, value));
		}
		return origValue;
	}

	V get(K key) {
		V value = NULL;
		std::vector<Pair<K, V>>::iterator it = items.begin();
		while (it != items.end() && doCompare_(it->first, key) < 0)
			++it;
		if (it != items.end() && doCompare_(it->first, key) == 0)
			value = it->second;
		return value;
	}

	bool containsKey(K key) {
		std::vector<Pair<K, V>>::iterator it = items.begin();
		while (it != items.end() && doCompare_(it->first, key) < 0)
			++it;

		return it != items.end() && doCompare_(it->first, key) == 0;
	}

	bool containsValue(V value) {
		std::vector<Pair<K, V>>::iterator it = items.begin();
		while (it != items.end() && !it->second->equals(value))
			++it;

		return it != items.end();
	}

	V remove(K key) {
		std::vector<Pair<K, V>>::iterator it = items.begin();
		while (it != items.end() && !it->first->equals(key))
			++it;

		V item = NULL;
		if (it != items.end()) {
			item = it->second;
			items.erase(it);
		}

		return item;
	}

	void clear() {
		items.clear();
	}

	java_util_Set<K>* keySet() {
		java_util_Set<V>* set = new java_util_HashSet<V>();
		std::vector<Pair<K, V>>::iterator it = items.begin();
		while (it != items.end()) {
			set->add(it->first);
			++it;
		}
		return set;

	}

	java_util_Collection<V>* values() {
		java_util_Collection<V>* vec = new java_util_ArrayList<V>();
		std::vector<Pair<K, V>>::iterator it = items.begin();
		while (it != items.end()) {
			vec->add(it->second);
			++it;
		}
		return vec;
	}

	java_util_Comparator<java_lang_Object*>* comparator() { return comparator_; }

	K firstKey() {
		java_lang_Object* min = NULL;

		if (items.size() > 0) {
			std::vector<Pair<K, V>>::iterator it = items.begin();
			min = it->first;			
		}

		return (K)min;
	}

	K lastKey() {
		java_lang_Object* max = NULL;

		if (items.size() > 0) {
			std::vector<Pair<K, V>>::iterator it = items.begin();
			max = it->first;
			++it;
			while (it != items.end()) {
				if (jcl_dynamicCast<java_lang_Comparable*>(it->first)->compareTo(max) > 0)
					max = it->first;
				++it;
			}
		}

		return (K)max;
	}
};


template<class T>
class java_util_HashSet : public java_util_ArrayList<T>, public java_util_Set<T> {
public:
	// Note: this implementation of equals works only for HashSets containing numeric values
	bool equals(java_lang_Object* o) {
		java_util_HashSet<T> *other = dynamic_cast<java_util_HashSet<T>*>(o);
		if (other) {
			if (other->size() != this->size())
				return false;

			std::vector<T>::iterator it = this->items.begin();
			while (it != this->items.end()) {
				if (!other->contains(*it))
					return false;
				++it;
			}

			return true;
		}
		return false;
	}
};

#define _calltestmethod(obj,testName,meth) \
 logstrm << testName << std::endl; \
 try { obj->setUp(); obj->meth(); obj->tearDown(); } catch(java_lang_Exception *e) { junit_framework_Assert::reportError(e->what(), __FILE__, __LINE__); }

#define _calltestmethod4(obj,testName,meth) \
 logstrm << testName << std::endl; \
 try { obj->meth(); } catch(java_lang_Exception *e) { junit_framework_Assert::reportError(e->what(), __FILE__, __LINE__); }

/*
class java_math_BigDecimal : public java_lang_Object {
public:
	static int ROUND_HALF_UP_;

	java_math_BigDecimal(double d);
	java_math_BigDecimal(int32_t d);
	java_math_BigDecimal(java_lang_String*);
	double doubleValue();
	java_math_BigDecimal *abs();
	java_math_BigDecimal *negate();
	java_math_BigDecimal *add(java_math_BigDecimal *);
	java_math_BigDecimal *divide(java_math_BigDecimal *, int32_t, int32_t);
	java_math_BigDecimal *multiply(java_math_BigDecimal *);
	java_math_BigDecimal *subtract(java_math_BigDecimal *);
	java_math_BigDecimal *max(java_math_BigDecimal *);
	int32_t compareTo(java_math_BigDecimal *);
};
*/

#include <java/math/BigDecimal.h>


#endif