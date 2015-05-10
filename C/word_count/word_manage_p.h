#ifndef WORD_MANAGE_P_H_INCLUDE
#define WORD_MANAGE_P_H_INCLUDE

#include"word_manage.h"

typedef struct Word_tag{
	char		*name;
	int			count;
	struct Word_tag	*next;
} Word;

extern Word *word_header;

#endif
