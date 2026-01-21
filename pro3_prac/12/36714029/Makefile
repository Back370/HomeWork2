CC = gcc
TAEGET = task14
SRCS = main14.c task14.c

all: $(TAEGET)

$(TAEGET) : $(SRCS)
	$(CC) -o $(TAEGET) $(SRCS)

run: $(TAEGET)

	./$(TAEGET)

clear: 
	rm -f $(TAEGET) $(TAEGET).exe

.PHONY: all run clean