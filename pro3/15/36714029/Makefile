CC = gcc
CFLAGS = -Wall -lm
TARGET = main15-1
OBJS = main15-1.o task15-1.o

all: $(TARGET)

$(TARGET): $(OBJS)
	$(CC) -o $(TARGET) $(OBJS) $(CFLAGS)

main15-1.o: main15-1.c task15-1.h
	$(CC) -c main15-1.c $(CFLAGS)

task15-1.o: task15-1.c task15-1.h
	$(CC) -c task15-1.c $(CFLAGS)

clean:
	rm -f $(OBJS) $(TARGET) *.dat

run: $(TARGET)
	./$(TARGET)
