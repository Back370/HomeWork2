CC = gcc
TARGET = task14
SRCS = main14.c task14.c

all: $(TARGET)

$(TARGET): $(SRCS) 
	$(CC) -o $(TARGET) $(SRCS)

run: $(TARGET)
	./$(TARGET)

clean:
	rm -f $(TARGET) $(TARGET).exe

.PHONY: all run clean