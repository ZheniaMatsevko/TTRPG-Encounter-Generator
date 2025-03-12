# Define variables
FRONTEND_DIR=frontend
BACKEND_DIR=backend

# Commands
install-front:
	@echo "Installing dependencies..."
	cd $(FRONTEND_DIR) && npm install

build-front:
	@echo "Building frontend and backend..."
	cd $(FRONTEND_DIR) && npm run build

start:
	@echo "Starting frontend and backend..."
	cd $(BACKEND_DIR) && npm run start &
	cd $(FRONTEND_DIR) && npm run dev

start-front:
	@echo "Starting frontend app"
	cd $(FRONTEND_DIR) && npm run dev

setup:
	@echo "Setting up project..."
	make install-front
	make build-front
