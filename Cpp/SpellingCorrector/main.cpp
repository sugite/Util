#include<iostream>
#include<unordered_map>
#include<string>
#include<fstream>
#include<regex>


using namespace std ;

unordered_map<string,int> NWORDS ;

void words(string FilePath)
{
	string s((istreambuf_iterator<char>(ifstream(FilePath))) , istreambuf_iterator<char>()); 
	string word ;
	for(int i = 0 ; i < s.length() ; ++i){
		if(isalpha(s[i])) word += tolower(s[i]);
		else if(word.size()){
			NWORDS[word]++;
			word.clear();
		}
	}
}

vector<string> edits(string word)
{
	vector<string> result ;
	//É¾³ý
	for(int i = 0; i < word.length(); ++i) 
		result.push_back(word.substr(0,i) + word.substr(i+1));
	//½»»»
	for(int i = 0; i < word.length() - 1; ++i) 
		result.push_back(word.substr(0, i) + word.substr(i+1, 1) + word.substr(i, 1) + word.substr(i+2));
	//´úÌæ
	for(int i = 0; i < word.length(); ++i) 
		for(char c ='a'; c <= 'z'; ++c) 
			result.push_back(word.substr(0, i) + c + word.substr(i+1));
	//²åÈë
	for(int i = 0; i <= word.length(); ++i) 
		for(char c ='a'; c <= 'z'; ++c) 
			result.push_back(word.substr(0, i) + c + word.substr(i));
	return result;
}

string correct(string word)
{
	if(NWORDS.find(word)!=NWORDS.end()) return word;
	vector<string> list = edits(word);
	unordered_map<int, string> candidates;
	for(int i = 0 ; i < list.size() ; ++i) 
		if(NWORDS.find(list[i])!=NWORDS.end()) 
			candidates[NWORDS[list[i]]] = list[i];
	if(candidates.size() > 0){
		int key = 0 ;
		for(unordered_map<int, string>::iterator itr = candidates.begin(); itr != candidates.end(); itr++){
			if(itr->first>key) key = itr->first;
		}
		return candidates[key];
	}
	for(int i = 0 ; i < list.size() ; ++i){ 
		vector<string> list1 = edits(list[i]);
		for(int j = 0 ; j < list1.size() ; ++j)
			if(NWORDS.find(list1[j])!=NWORDS.end()) 
				candidates[NWORDS[list1[j]]] = list1[j];
	}
	if(candidates.size() > 0){
		int key = 0 ;
		for(unordered_map<int, string>::iterator itr = candidates.begin(); itr != candidates.end(); itr++){
			if(itr->first>key) key = itr->first;
		}
		return candidates[key];
	}
	return word;
}

int main(int argc, char **argv)
{
	words("big.txt");
	string word ;
	while(1){
		cout<<"input a word:"<<endl;
		cin>>word;
		cout<<correct(word)<<endl;
	}
	return 0;
}