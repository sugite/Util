#include"TextQuery.h"
#include<sstream>
#include<string>
#include<vector>
#include<map>
#include<set>
#include<iostream>
#include<fstream>
#include<cctype>
#include<cstring>
#include<stdexcept>

using namespace std;

string TextQuery::text_line(line_no line) const 
{
	if(line < lines_of_text.size())
		return lines_of_text[line];
	throw out_of_range("line number out of range");
}

void TextQuery::store_file(ifstream &is)
{
	string textline;
	while(getline(is, textline))
		lines_of_text.push_back(textline);
}

string TextQuery::whitespace_chars(" \t\n\v\r\f");

void TextQuery::build_map()
{
	for(line_no line_num = 0 ; line_num != lines_of_text.size() ; ++line_num){
		istringstream line(lines_of_text[line_num]);
		string word;
		while(line >> word)
			word_map[cleanup_str(word)].insert(line_num);
	}
}

set<TextQuery::line_no> TextQuery::run_query(const string &query_word) const
{
	map<string, set<line_no> >::const_iterator loc = word_map.find(cleanup_str(query_word));
	if(loc == word_map.end()) return set<line_no>();
	else return loc->second;
}

void TextQuery::display_map()
{
	map<string, set<line_no> >::iterator iter = word_map.begin(),
		iter_end = word_map.end();
	for(; iter != iter_end ; ++iter){
		cout<< "word: " <<iter->first << " {";
		const set<line_no> &text_locs = iter->second;
		set<line_no>::const_iterator loc_iter = text_locs.begin(),
			loc_iter_end = text_locs.end();

		while(loc_iter != loc_iter_end){
			cout<< *loc_iter ;
			if(++loc_iter != loc_iter_end)
				cout<<", ";
		}

		cout<<"}\n";
	}
	cout<<endl;
}

string TextQuery::cleanup_str(const string &word)
{
	string ret;
	for(string::const_iterator it = word.begin() ; it != word.end() ; ++it){
		if(!ispunct(*it))
			ret += tolower(*it);
	}
	return ret;
}