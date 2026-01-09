# Makefile for Esiéamon project

# Java compiler
JAVAC = javac
JAVA = java

# Directories
SRC_DIR = src
BIN_DIR = classes

# Main package
MAIN_CLASS = com.esiea.pootp.EsieamonExecutable

# Source files (recursive retrieval of all .java files)
SOURCES = $(shell find $(SRC_DIR) -name "*.java")

# Compiled class files
CLASSES = $(SOURCES:$(SRC_DIR)/%.java=$(BIN_DIR)/%.class)

# Default target
.PHONY: all
all: compile

# Compilation
.PHONY: compile
compile: $(BIN_DIR)
	@$(JAVAC) --release 17 -d $(BIN_DIR) -sourcepath $(SRC_DIR) $(SOURCES)

# Create classes directory if it doesn't exist
$(BIN_DIR):
	@mkdir -p $(BIN_DIR)

# Execution
.PHONY: run
run: compile
	@$(JAVA) -cp $(BIN_DIR) $(MAIN_CLASS)

# Clean
.PHONY: clean
clean:
	@echo "Cleaning compiled files..."
	rm -rf $(BIN_DIR)
	@echo "Cleaning complete!"

# Full rebuild
.PHONY: rebuild
rebuild: clean all

# Help
.PHONY: help
help:
	@echo "Makefile for Esiéamon"
	@echo ""
	@echo "Available commands:"
	@echo "  make          - Compile the project"
	@echo "  make compile  - Compile the project"
	@echo "  make run      - Compile and run the game"
	@echo "  make clean    - Remove compiled files"
	@echo "  make rebuild  - Clean and recompile the project"
	@echo "  make help     - Display this help"