#include"newQuery.h"
#include"TextQuery.h"
#include<set>
#include<algorithm>
#include<iostream>
#include<iterator>

using namespace std;

set<TextQuery::line_no> NotQuery::eval(const TextQuery &file) const
{	
	set<TextQuery::line_no> has_val = query.eval(file);
	set<TextQuery::line_no> ret_lines;
	for(TextQuery::line_no n = 0 ; n != file.size() ; ++n)
		if(has_val.find(n) == has_val.end())
			ret_lines.insert(n);
	return ret_lines;
}

set<TextQuery::line_no> AndQuery::eval(const TextQuery& file) const
{
    
    set<TextQuery::line_no> left = lhs.eval(file), 
                 right = rhs.eval(file);

    set<TextQuery::line_no> ret_lines; 
    set_intersection(left.begin(), left.end(), 
                  right.begin(), right.end(),
                  inserter(ret_lines, ret_lines.begin()));
    return ret_lines;
}

set<TextQuery::line_no> OrQuery::eval(const TextQuery& file) const
{
   
    set<TextQuery::line_no> right = rhs.eval(file),
             ret_lines = lhs.eval(file); 
	ret_lines.insert(right.begin(), right.end());

    return ret_lines;
}

ifstream& open_file(ifstream &in,const string &file)
{
	in.close();
	in.clear();
	in.open(file.c_str());
	return in;
}

TextQuery build_textfile(const string &filename)
{
    // get a file to read from which user will query words
    ifstream infile;
    if (!open_file(infile, filename)) {
        cerr << "No input file!" << endl;
        return TextQuery();
    }

    TextQuery ret;
    ret.read_file(infile);  // builds query map
    return ret;  // builds query map
}

bool get_word(string &s1)
{
    cout << "enter a word to search for, or q to quit: ";
    cin >> s1;
    if (!cin || s1 == "q") return false;
    else return true;
}

bool get_words(string &s1, string &s2)
{

    // iterate with the user: prompt for a word to find and print results
    cout << "enter two words to search for, or q to quit: ";
    cin  >> s1;

    // stop if hit eof on input or a "q" is entered
    if (!cin || s1 == "q") return false;
    cin >> s2;
    return true;
}

void print_results(const set<TextQuery::line_no>& locs, const TextQuery &file)
{
    // report no matches
    if (locs.empty()) {
        cout << "\nSorry. There are no entries for your query." 
             << "\nTry again." << endl;
        return;
    }

    // if the word was found, then print count and all occurrences
    set<TextQuery::line_no>::size_type size = locs.size();
    cout << "match occurs " 
         << size << (size == 1 ? " time:" : " times:") << endl;

    // print each line in which the word appeared
    set<TextQuery::line_no>::const_iterator it = locs.begin();
    for ( ; it != locs.end(); ++it) {
        cout << "\t(line "
             // don't confound user with text lines starting at 0
             << (*it) + 1 << ") "
             << file.text_line(*it) << endl;
    }
}