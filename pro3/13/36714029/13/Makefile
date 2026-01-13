CC = gcc
CFLAGS = -Wall -g
TARGET = task13
OBJS = main13.o task13.o

all: $(TARGET)

$(TARGET): $(OBJS)
	$(CC) $(CFLAGS) -o $(TARGET) $(OBJS)

main13.o: main13.c task13.h
	$(CC) $(CFLAGS) -c main13.c

task13.o: task13.c task13.h
	$(CC) $(CFLAGS) -c task13.c

run: $(TARGET)
	./$(TARGET)

clean:
	rm -f $(OBJS) $(TARGET) $(TARGET).exe

.PHONY: all run clean
