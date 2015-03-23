#pragma once
#include<string>
#include<vector>
#include<map>
#include<set>
#include<iostream>
#include<fstream>
#include<cctype>
#include<cstring>
using namespace std ;
class TextQuery{
public:
	typedef string::size_type str_size;
	typedef vector<string>::size_type line_no;

	void read_file(ifstream &is)
	{
		store_file(is);
		build_map();
	}

	set<line_no> run_query(const string&) const;
	string text_line(line_no) const;
	str_size size() const { return lines_of_text.size(); }
	void display_map();

private:
	void store_file(ifstream &);
	void build_map();

	vector<string> lines_of_text;

	map<string, set<line_no> > word_map;
	static string whitespace_chars;
	static string cleanup_str(const string&);
};