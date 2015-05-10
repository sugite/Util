#include<stdlib.h>
#include"word_manage_p.h"

void word_finalize(void)
{
	Word *temp ; 
	while(word_header){
		temp = word_header;
		word_header = word_header->next ;
		free(temp->name);
		free(temp);
	}
}
