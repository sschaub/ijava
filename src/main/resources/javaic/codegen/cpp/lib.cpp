
#include "lib.h"
#include <sstream>
#include <iomanip>
#include <algorithm>
#include <cctype>

std::ofstream logstrm;

double java_lang_Double::NaN_ = std::numeric_limits<double>::quiet_NaN();

double java_lang_Double::MIN_VALUE_ = std::numeric_limits<double>::min();

double java_lang_Double::MAX_VALUE_ = std::numeric_limits<double>::max();

double java_lang_Double::POSITIVE_INFINITY_ = std::numeric_limits<double>::infinity();

double java_lang_Double::NEGATIVE_INFINITY_ = -std::numeric_limits<double>::infinity();

int32_t java_lang_Integer::MIN_VALUE_ = std::numeric_limits<int32_t>::min();

int32_t java_lang_Integer::MAX_VALUE_ = std::numeric_limits<int32_t>::max();

int64_t java_lang_Long::MAX_VALUE_ = std::numeric_limits<int64_t>::max();

int64_t java_lang_Long::MIN_VALUE_ = std::numeric_limits<int64_t>::min();

java_lang_Boolean *java_lang_Boolean::TRUE_ = new java_lang_Boolean(true);

class StringInsensitiveComparator : public java_util_Comparator < java_lang_Object* > {
	int32_t compare(java_lang_Object * o1, java_lang_Object * o2) {
		java_lang_String* s1 = jcl_dynamicCast<java_lang_String*>(o1);
		java_lang_String* s2 = jcl_dynamicCast<java_lang_String*>(o2);
		std::string str1 = s1->str;
		std::string str2 = s2->str;
		std::transform(str1.begin(), str1.end(), str1.begin(), std::toupper);
		std::transform(str2.begin(), str2.end(), str2.begin(), std::toupper);
		return str1.compare(str2);
	}
};

java_util_Comparator<java_lang_Object*>* java_lang_String::CASE_INSENSITIVE_ORDER_ = new StringInsensitiveComparator();


double java_lang_Math::PI_ = 3.14159265358979323846;

java_io_PrintStream* java_lang_System::out_ = new java_io_PrintStream(std::cout);

java_io_PrintStream* java_lang_System::err_ = new java_io_PrintStream(std::cerr);

java_lang_String* java_lang_Object::toString() {
    return new java_lang_String(typeid(this).name());
}

java_lang_Throwable::java_lang_Throwable() : msg(NULL) { }
java_lang_Throwable::java_lang_Throwable(java_lang_String *msg) : msg(msg) { }
const char* java_lang_Throwable::what() const {
	return msg == NULL ? msg->str.c_str() : typeid(this).name();
}

int32_t java_lang_Integer::parseInt(java_lang_String* s, int32_t) {
	return atoi(s->str.c_str());
}

int64_t java_lang_Math::round(double value)
{
    if (value > java_lang_Long::MAX_VALUE_)
        return java_lang_Long::MAX_VALUE_;
    else if (value < java_lang_Long::MIN_VALUE_)
        return java_lang_Long::MIN_VALUE_;
    else if (std::isnan(value))
        return 0;
    else
        return std::round(value);
}

java_lang_String* jcl_tostr(int8_t value) {
	return new java_lang_String(std::to_string(value));
}

java_lang_String* jcl_tostr(int16_t value) {
	return new java_lang_String(std::to_string(value));
}

java_lang_String* jcl_tostr(int32_t value) {
	return new java_lang_String(std::to_string(value));
}

java_lang_String* jcl_tostr(int64_t value) {
    return new java_lang_String(std::to_string(value));
}

java_lang_String* jcl_tostr(double value) {
	if (isnan(value))
		return new java_lang_String("NaN");
	double val = 0.1;
	std::stringstream ss;
	ss << std::setprecision(15) << std::scientific << value;
	return new java_lang_String(ss.str());
}

java_lang_String* jcl_tostr(char value) {
    return new java_lang_String(std::string(1, value));
}

java_lang_String* jcl_tostr(bool value) {
	return value ? new java_lang_String("true") : new java_lang_String("false");
}

uint64_t jcl_tounsigned(int8_t num) {
	return (uint8_t)num;
}

uint64_t jcl_tounsigned(int16_t num) {
	return (uint16_t)num;
}


uint64_t jcl_tounsigned(int32_t num) {
	return (uint32_t)num;
}

uint64_t jcl_tounsigned(int64_t num) {
	return (uint64_t)num;
}
java_lang_String::java_lang_String(Array<char>* chars, int32_t offset, int32_t count) {

	for (int i = 0; i < count; ++i) {
		str += chars->AT(offset + i);
	}

}

int64_t java_lang_Long::parseLong(java_lang_String* str, int32_t) {
	return atol(str->str.c_str());
}

void java_lang_Throwable::printStackTrace(java_io_PrintWriter* out) {
	out->print(msg);
}

java_lang_String* java_lang_Integer::toString(int32_t num, int32_t radix) {
	return jcl_tostr(num);
}


java_lang_String* java_lang_Long::toString(int64_t num, int32_t radix) {
	return jcl_tostr(num);
}

void java_lang_StringBuffer::append(Array<char>* chars, int32_t start, int32_t end) {
	for (int i = start; i < end; ++i) {
		value += chars->AT(i);
	}
}

void junit_framework_Assert::assertEquals(double expected, double actual, double tolerance, std::string file, int line) {
	assertEquals(NULL, expected, actual, tolerance, file, line);
}


void junit_framework_Assert::assertEquals(java_lang_String* msg, double expected, double actual, double tolerance, std::string file, int line) {
	double displayTolerance = (tolerance == java_lang_Double::MIN_VALUE_) ? 0 : tolerance;

	if ((expected < -DBL_MAX && actual < -DBL_MAX) || (expected > DBL_MAX && actual > DBL_MAX)) {
		std::string str = msg ? "\"" + msg->str + "\"" : "null";
		std::string val = (expected < -DBL_MAX) ? "-inf" : "inf";
		if (tolerance == java_lang_Double::MIN_VALUE_)
			tolerance = 0;
		if (logstrm.is_open())
			logstrm << "assertEquals:" << str << "`" << val << "`" << val << "`" << jcl_tostr(displayTolerance)->str << std::endl;
		return;
	}

	std::string str = msg ? "\"" + msg->str + "\"" : "null";
	if (logstrm.is_open())
		logstrm << "assertEquals:" << str << "`" << jcl_tostr(expected)->str << "`" << jcl_tostr(actual)->str << "`" << jcl_tostr(displayTolerance)->str << std::endl;
	if (abs(expected - actual) > tolerance)
		reportError(msg ? msg->str : "assertEquals failed", file, line);
}

void junit_framework_Assert::assertEquals(java_lang_String* msg, int64_t expected, int64_t actual, std::string file, int line) {
	std::string str = msg ? "\"" + msg->str + "\"" : "null";
	if (logstrm.is_open())
		logstrm << "assertEquals:" << str << "`" << expected << "`" << actual << "`0" << std::endl;
	if (expected != actual)
		reportError(msg ? msg->str : "assertEquals failed", file, line);
}

void junit_framework_Assert::assertEquals(java_lang_String* expected, java_lang_String* actual, std::string file, int line) {
	junit_framework_Assert::assertEquals(NULL, expected, actual, file, line);
}

void junit_framework_Assert::assertEquals(java_lang_String *msg, java_lang_String* expected, java_lang_String* actual, std::string file, int line) {
	std::string str = msg ? "\"" + msg->str + "\"" : "null";
	std::string strExpected = expected ? "\"" + expected->str + "\"" : "null";
	std::string strActual = actual ? "\"" + actual->str + "\"" : "null";
	if (logstrm.is_open())
		logstrm << "assertEqualsStr:" << str << "`" << strExpected << "`" << strActual << std::endl;
	if (strExpected != strActual)
		reportError(msg ? msg->str : "assertEquals failed", file, line);
}

void junit_framework_Assert::assertEquals(java_lang_Object* expected, java_lang_Object* actual, std::string file, int line) {
	junit_framework_Assert::assertEquals(NULL, expected, actual, file, line);
}

void junit_framework_Assert::assertEquals(java_lang_String *msg, java_lang_Object* expected, java_lang_Object* actual, std::string file, int line) {
	if (dynamic_cast<java_lang_String*>(expected) && dynamic_cast<java_lang_String*>(actual)) {
		assertEquals(msg, dynamic_cast<java_lang_String*>(expected), dynamic_cast<java_lang_String*>(actual), file, line);
	}
	else {
		std::string str = msg ? "\"" + msg->str + "\"" : "null";
		if (logstrm.is_open())
			logstrm << "assertEqualsObj:" << str << "`" << ((expected == NULL) ? "null" : "notnull") << "`" << ((actual == NULL) ? "null" : "notnull") << std::endl;
		if (!(expected == actual || (expected != NULL && actual != NULL && expected->equals(actual))))
			reportError(msg ? msg->str : "assertEquals failed", file, line);
	}
}

void junit_framework_Assert::assertEquals(int64_t expected, int64_t actual, std::string file, int line) {
	assertEquals(NULL, expected, actual, file, line);
}

void junit_framework_Assert::assertTrue(bool cond, std::string file, int line) {
	assertTrue(NULL, cond, file, line);
}

void junit_framework_Assert::assertTrue(java_lang_String* msg, bool cond, std::string file, int line) {
	std::string str = msg ? "\"" + msg->str + "\"" : "null";
	if (logstrm.is_open())
		logstrm << "assertTrue:" << str << "`" << (cond ? "true" : "false") << std::endl;

	if (!cond)
		reportError(msg ? msg->str : "assertTrue failed", file, line);
}

void junit_framework_Assert::assertFalse(bool cond, std::string file, int line) {
	assertTrue(!cond, file, line);
}

void junit_framework_Assert::assertFalse(java_lang_String* msg, bool cond, std::string file, int line) {
	assertTrue(msg, !cond, file, line);
}

void junit_framework_Assert::assertNull(void* obj, std::string file, int line) {
	if (logstrm.is_open())
		logstrm << "assertNull:" << (obj == NULL ? "true" : "false") << std::endl;

	if (!(obj == NULL))
		reportError("assertNull failed", file, line);
}

void junit_framework_Assert::assertNull(java_lang_String* msg, void* obj, std::string file, int line) {
	assertTrue(msg, obj == NULL, file, line);
}

void junit_framework_Assert::assertNotNull(void* obj, std::string file, int line) {
	assertNotNull(NULL, obj, file, line);
}

void junit_framework_Assert::assertNotNull(java_lang_String *msg, void* obj, std::string file, int line) {
	assertTrue(msg, obj != NULL, file, line);
}

void junit_framework_Assert::fail(java_lang_String* msg, std::string file, int line) {
	reportError(msg->str, file, line);
}

void junit_framework_Assert::fail(std::string file, int line) {
	reportError("Uh oh", file, line);
}

void junit_framework_Assert::reportError(std::string msg, std::string file, int line) {
	if (logstrm.is_open())
		std::cerr << file << "(" << line << "): " << msg << std::endl;
}

