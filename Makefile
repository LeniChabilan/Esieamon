# Makefile for Esiéamon project

# Java compiler
JAVAC = javac
JAVA = java

# Directories
SRC_DIR = src
BIN_DIR = classes
LIB_DIR = lib

# Detect OS and set path separator
UNAME_S := $(shell uname -s)
ifeq ($(UNAME_S),Linux)
    OS_TYPE = linux
    PATH_SEP = :
endif
ifeq ($(UNAME_S),Darwin)
    OS_TYPE = mac
    PATH_SEP = :
endif
ifeq ($(OS),Windows_NT)
    OS_TYPE = win
    PATH_SEP = ;
endif

# JavaFX JARs (local) - platform-specific
JAVAFX_PATH = $(LIB_DIR)/javafx-base-21.0.1-$(OS_TYPE).jar$(PATH_SEP)$(LIB_DIR)/javafx-graphics-21.0.1-$(OS_TYPE).jar$(PATH_SEP)$(LIB_DIR)/javafx-controls-21.0.1-$(OS_TYPE).jar$(PATH_SEP)$(LIB_DIR)/javafx-fxml-21.0.1-$(OS_TYPE).jar

# Main package
MAIN_CLASS = com.esiea.pootp.EsieamonExecutable

# Source files (recursive retrieval of all .java files)
SOURCES = $(shell find $(SRC_DIR) -name "*.java")

# Default target
.PHONY: all
all: compile

# Compilation with JavaFX (classpath only)
.PHONY: compile
compile: $(BIN_DIR)
	@$(JAVAC) -d $(BIN_DIR) -sourcepath $(SRC_DIR) -cp $(JAVAFX_PATH) $(SOURCES)

# Create classes directory if it doesn't exist
$(BIN_DIR):
	@mkdir -p $(BIN_DIR)

# Execution with JavaFX (classpath only)
.PHONY: run
run: compile
	@$(JAVA) -cp $(BIN_DIR)$(PATH_SEP)$(JAVAFX_PATH) $(MAIN_CLASS)

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