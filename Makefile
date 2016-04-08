# Makefile for "The green elevator project" for course ID1217 at KTH
#
# Authors:	Rasmus Linusson <raslin@kth.se>
#	 		Karl Gafvert <kalleg@kth.se>
#
# Last modified: 17/03-2016

# Directories
DIR_SRC = ./
DIR_OBJ = ./obj
DIR_HEADERS = ./include

# Compilation and linking flags
CC = gcc
CFLAGS = -Wall -c -I$(DIR_HEADERS)
LDFLAGS = -lpthread -lm

ifdef WDISTANCE
CFLAGS += -DSCORE_WEIGHT_DISTANCE=$(WDISTANCE)
endif
ifdef WSTOPS
CFLAGS += -DSCORE_WEIGHT_STOPS=$(WSTOPS)
endif


# Release flags
RLS_CFLAGS = -O4

# Debugging flags
DBG_CFLAGS = -g

# src and objs
SRC_FLAT := $(shell find $(DIR_SRC) -maxdepth 1 -name '*.c' -printf '%P\n')
OBJ := $(addprefix $(DIR_OBJ)/,$(SRC_FLAT:%.c=%.o))

# Targets
.PHONY: all debug clean

# Compile with release flags
all: CFLAGS += $(RLS_CFLAGS)
all: controller

# Compile with debugging flags
debug: CFLAGS += $(DBG_CFLAGS)
debug: controller

controller: $(OBJ)
	$(CC) -o $@ $(OBJ) $(LDFLAGS) 

$(DIR_OBJ)/%.o: $(DIR_SRC)/%.c
	$(CC) $(CFLAGS) -o $@ $<

clean:
	-rm -rf controller test $(DIR_OBJ)/*.o
