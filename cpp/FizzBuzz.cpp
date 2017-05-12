#include <iostream>
#include <string>
using namespace std;

int main()
{
	for (int i = 1; (i & 0x0FF) <= 100; ++i &= 0x0FF){
		cout << (!((i & 0x0FF) % 3) ? (i |= 0x100), "Fizz" : "") << (!((i & 0x0FF) % 5) ? (i |= 0x100), "Buzz" : ""), ((i & 0x100) ? cout << "" : cout<< i), cout << "\n"; 
	}
}
