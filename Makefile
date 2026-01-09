# Makefile for Esiéamon project

# Java compiler
JAVAC = javac
JAVA = java

# Directories
SRC_DIR = src
BIN_DIR = classes
LIB_DIR = lib

# JavaFX JARs (local)
JAVAFX_PATH = $(LIB_DIR)/javafx-base-21.0.1-linux.jar:$(LIB_DIR)/javafx-graphics-21.0.1-linux.jar:$(LIB_DIR)/javafx-controls-21.0.1-linux.jar:$(LIB_DIR)/javafx-fxml-21.0.1-linux.jar

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
	@$(JAVAC) --release 17 -d $(BIN_DIR) -sourcepath $(SRC_DIR) -cp $(JAVAFX_PATH) $(SOURCES)
	@echo "Copying resources..."
	@cp -r $(SRC_DIR)/com/esiea/pootp/assets $(BIN_DIR)/com/esiea/pootp/ 2>/dev/null || true
	@cp $(SRC_DIR)/com/esiea/pootp/Parser/game_data.txt $(BIN_DIR)/com/esiea/pootp/Parser/ 2>/dev/null || true

# Create classes directory if it doesn't exist
$(BIN_DIR):
	@mkdir -p $(BIN_DIR)

# Execution with JavaFX (classpath only)
.PHONY: run
run: compile
	@$(JAVA) -cp $(BIN_DIR):$(JAVAFX_PATH) $(MAIN_CLASS)

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