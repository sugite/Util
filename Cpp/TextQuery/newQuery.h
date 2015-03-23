#pragma once
#include"TextQuery.h"
#include<string>
#include<set>
#include<iostream>
#include<fstream>

using namespace std ;

class Query_base{
	friend class Query;
protected:
	typedef TextQuery::line_no line_no;
	virtual ~Query_base(){}
private:
	virtual set<line_no> eval(const TextQuery&) const = 0 ;
	virtual ostream& display(ostream& = cout) const = 0;
};

class Query{
	friend Query operator~(const Query &);
	friend Query operator|(const Query&, const Query&);
	friend Query operator&(const Query&, const Query&);
public:
	Query(const string&);

	Query(const Query &c):q(c.q),use(c.use){++*use;}
	~Query(){ decr_use();}
	Query& operator=(const Query&);

	set<TextQuery::line_no> eval(const TextQuery &t) const {return q->eval(t);}
	ostream& display(ostream &os) const {return q->display(os);}
private:
	Query(Query_base *query):q(query),use(new size_t(1)){}
	Query_base *q;
	size_t *use;
	void decr_use()
	{
		if(--*use == 0)
		{
			delete q;
			delete use;
			q = NULL;
			use = NULL;
		}
	}
};

inline ostream& operator<<(ostream &os, const Query &q)
{
	return q.display(os);
}


inline Query& Query::operator=(const Query &rhs)
{
    ++*rhs.use; 
    decr_use(); 
    q = rhs.q; 
    use = rhs.use; 
    return *this; 
}

class WordQuery : public Query_base{
	friend class Query;
	WordQuery(const string &s):query_word(s){}
	set<line_no> eval(const TextQuery &t) const 
	{ return t.run_query(query_word);}
	ostream& display(ostream &os) const 
	{ return os<<query_word;}
	string query_word;
};

inline Query::Query(const std::string &s): q(new WordQuery(s)),
                               use(new std::size_t(1)) { }

class NotQuery : public Query_base{
	friend Query operator~(const Query &);
	NotQuery(Query q):query(q){}
	set<line_no> eval(const TextQuery&) const;
    ostream& display(ostream &os) const
          { return os << "~(" << query << ")"; }
	const Query query;
};

class BinaryQuery : public Query_base{
protected:
	BinaryQuery(Query left, Query right, string op):
		 lhs(left),rhs(right),oper(op){}
	ostream& display(ostream &os) const{
		return  os << "(" << lhs  << " " << oper << " " 
                << rhs << ")";
	}

	const Query lhs, rhs;
	const string oper;
};

class AndQuery: public BinaryQuery {
    friend Query operator&(const Query&, const Query&);
    AndQuery(Query left, Query right): 
                        BinaryQuery(left, right, "&") { }

    std::set<line_no> eval(const TextQuery&) const;
};

class OrQuery: public BinaryQuery {
    friend Query operator|(const Query&, const Query&);
    OrQuery(Query left, Query right): 
                BinaryQuery(left, right, "|") { }

    std::set<line_no> eval(const TextQuery&) const;
};


inline Query operator&(const Query &lhs, const Query &rhs)
{
	return new AndQuery(lhs, rhs);
}

inline Query operator|(const Query &lhs, const Query &rhs)
{
	return new OrQuery(lhs, rhs);
}

inline Query operator~(const Query &rhs)
{
	return new NotQuery(rhs);
}

std::ifstream& open_file(std::ifstream&, const std::string&);
TextQuery build_textfile(const std::string&);
bool get_word(std::string&);
bool get_words(std::string&, std::string&);
void print_results(const std::set<TextQuery::line_no>&, const TextQuery&);