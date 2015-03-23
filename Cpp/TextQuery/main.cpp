#include"newQuery.h"
#include"TextQuery.h"
#include<string>
#include<set>
#include<iostream>

using namespace std ;

int main()
{
    TextQuery file = build_textfile("C:\\Users\\Administrator\\Desktop\\MS_files\\15\\data\\Alice_story");

    do {
        string sought1, sought2;
        if (!get_words(sought1, sought2)) break;
    
        Query andq = Query(sought1) & Query(sought2);
        set<TextQuery::line_no> locs = andq.eval(file);
        cout << "\nExecuted query: " << andq << endl;
        print_results(locs, file);
    
        locs = (~Query(sought1)).eval(file);
        cout << "\nExecuted query: " << ~Query(sought1) << endl;
        print_results(locs, file);
    
        andq = Query(sought1) | Query(sought2);
		locs = andq.eval(file);
        cout << "\nExecuted query: " << andq << endl;
        print_results(locs, file);
    } while(true);
    return 0;
}