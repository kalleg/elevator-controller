# Makefile for "The green elevator project" for course ID1217 at KTH
#
# Authors:	Rasmus Linusson <raslin@kth.se>
#	 		Karl Gafvert <kalleg@kth.se>
#
# Last modified: 17/03-2016

# Compilation and linking flags
CC = gcc
CFLAGS = -Wall
LDFLAGS = -lpthread

# Release flags
RLS_CFLAGS = -O4

# Debugging flags
DBG_CFLAGS = -g

# Targets
.PHONY: all debug clean

# Compile with release flags
all: CFLAGS += $(RLS_CFLAGS)
all: controller 

# Compile with debugging flags
debug: CFLAGS += $(DBG_CFLAGS)
debug: controller

controller: api.o
	$(CC) $(CFLAGS) -o $@ $@.c $< $(LDFLAGS)

test: api.o
	$(CC) $(CFLAGS) -o $@ $@.c $< $(LDFLAGS)

api.o:
	$(CC) $(CFLAGS) -c -o $@ hardwareAPI.c

clean:
	-rm -rf controller test *.o
