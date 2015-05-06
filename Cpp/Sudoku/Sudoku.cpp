#include<iostream>
#include<string>
#include<vector>
#include<assert.h>
#include<ctime>

using std::cout		;
using std::endl		;
using std::vector	;
using std::swap		;

class Sudoku{
public:
	Sudoku():initNumCount(25),boardSize(9),
		board(boardSize,vector<char>(boardSize,'0')){}
	Sudoku(int numCount):initNumCount(numCount),boardSize(9),
		board(boardSize,vector<char>(boardSize,'0')){}
	Sudoku(const vector< vector<char> > &) ;
	vector< vector<char> > GetBoard() const { return board ; } 
	void GenerateBoard() ;
	void PrintBoard() const ;
	bool SolveSudoku() ;
private:
	bool DFS(int);
	bool validNum(int,int);
	int boardSize ;
	int initNumCount ;
	vector< vector<char> > board ;
};

Sudoku::Sudoku(const vector< vector<char> > &srcBoard)
{
	initNumCount = 0;
	boardSize = 9;
	assert(srcBoard.size()==boardSize);
	assert(srcBoard[0].size()==boardSize);
	for(int i = 0 ; i < boardSize ; ++i){
		for(int j = 0 ; j < boardSize ; ++j){
			if((board[i][j] = srcBoard[i][j]) != '0') 
				++initNumCount;
		}
	}
}

void Sudoku::PrintBoard() const
{
	for(int i = 0 ; i < boardSize ; ++i){
		for(int j = 0 ; j < boardSize ; ++j){
			cout<<board[i][j]<<" ";
			if(j%3 == 2) cout<<" ";
		}
		cout<<endl;
		if(i%3 == 2) cout<<endl;
	}
}

void Sudoku::GenerateBoard()
{
	int seq[9] = {1,2,3,4,5,6,7,8,9};
	std::srand((unsigned int)std::time(0));
	for(int i = boardSize-1 ; i > 0 ; --i) swap(seq[i],seq[rand()%i]); 
	//init center
	for(int i = 3 , k = 0 ; i < 6 ; ++i) for(int j = 3 ; j < 6 ; ++j) board[i][j] = seq[k++] + '0';
	//init left 
	for(int i = 3 , k = 3 ; i < 6 ; ++i) for(int j = 0 ; j < 3 ; ++j) board[i][j] = seq[(k++)%boardSize] + '0';
	//init right
	for(int i = 3 , k = 6 ; i < 6 ; ++i) for(int j = 6 ; j < 9 ; ++j) board[i][j] = seq[(k++)%boardSize] + '0';
	//init up
	for(int i = 0 ; i < 3 ; ++i) for(int j = 3 ; j < 6 ; ++j) board[i][j] = board[i+3][(j-2)%3+3];
	//init down
	for(int i = 6 ; i < 9 ; ++i) for(int j = 3 ; j < 6 ; ++j) board[i][j] = board[i-3][(j-1)%3+3];
	//init left up
	for(int i = 0 ; i < 3 ; ++i) for(int j = 0 ; j < 3 ; ++j) board[i][j] = board[i+3][(j+1)%3];
	//init left down
	for(int i = 6 ; i < 9 ; ++i) for(int j = 0 ; j < 3 ; ++j) board[i][j] = board[i-3][(j+2)%3];
	//init right up
	for(int i = 0 ; i < 3 ; ++i) for(int j = 6 ; j < 9 ; ++j) board[i][j] = board[i+3][(j-4)%3+6];
	//init right down
	for(int i = 6 ; i < 9 ; ++i) for(int j = 6 ; j < 9 ; ++j) board[i][j] = board[i-3][(j-5)%3+6];

	std::srand((unsigned int)std::time(0));

	//rand swap row
	for(int i = 0 ; i < 3 ; ++i){
		int seed = rand()%3 ;
		int row1 = (seed>1?1:0) + 3*i, row2 = (seed<1?1:2) +3*i;
		for(int col = 0 ; col < boardSize ; ++col){
			swap(board[row1][col],board[row2][col]);
		}
	}
	//rand swap col
	for(int i = 0 ; i < 3 ; ++i){
		int seed = rand()%3 ;
		int col1 = (seed>1?1:0) + 3*i, col2 = (seed<1?1:2) +3*i;
		for(int row = 0 ; row < boardSize ; ++row){
			swap(board[row][col1],board[row][col2]);
		}
	}

	//rand delete grid
	vector<int> vec(boardSize*boardSize,0) ;
	for(size_t i = 0 ; i < vec.size() ; ++i) vec[i] = i;
	for(int i = vec.size() - 1 ; i > 0 ; --i) swap(vec[i],vec[rand()%i]);
	for(int i = vec.size() - 1 - initNumCount ; i >= 0 ; --i) board[vec[i]/boardSize][vec[i]%boardSize] = '0';
}

bool Sudoku::validNum(int pos, int val){
	int row = pos/boardSize , col = pos%boardSize ;
	char ch = val + '0';
	//valid row
	for(int i = 0 ; i < boardSize ; ++i) if(board[row][i] == ch) return false;
	//valid col
	for(int i = 0 ; i < boardSize ; ++i) if(board[i][col] == ch) return false;
	//valid grid
	row = row/3*3 + 3, col = col/3*3 + 3;
	for(int i = row - 3 ; i < row; ++i) for(int j = col - 3 ; j < col ; ++j) if(board[i][j] == ch) return false;
	return true;
}

bool Sudoku::DFS(int n)
{
	if(n == boardSize*boardSize) return true; //succ
	if(board[n/boardSize][n%boardSize] != '0') return DFS(n+1);
	else{
		for(int i = 1 ; i <= boardSize ; ++i){
			if(validNum(n,i)){
				board[n/boardSize][n%boardSize] = '0' + i;
				if(DFS(n+1)) return true;
				board[n/boardSize][n%boardSize] = '0';
			}
		}
	}
	return false;
}

bool Sudoku::SolveSudoku()
{
	return DFS(0);
}

int main()
{
	Sudoku sudoku;
	sudoku.GenerateBoard();
	sudoku.PrintBoard();
	cout<<"Solution is : -----\n"<<endl;
	sudoku.SolveSudoku();
	sudoku.PrintBoard();
	return 0;
}