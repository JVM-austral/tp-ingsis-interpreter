#!/bin/bash

HOOK_DIR=".git/hooks"
HOOK_FILE="$HOOK_DIR/pre-commit"

# Crear el directorio si no existe
if [ ! -d "$HOOK_DIR" ]; then
  echo "Error: No se encontró el directorio .git/hooks. ¿Estás en la raíz de un repositorio git?"
  exit 1
fi

# Crear el hook
cat > "$HOOK_FILE" << 'EOF'
#!/bin/sh

# Verificar formato
./gradlew spotlessCheck
if [ $? -ne 0 ]; then
  echo "Spotless detectó problemas. Commit abortado."
  exit 1
fi

echo "Spotless OK. Continuando con el commit..."
EOF

# Dar permisos de ejecución
chmod +x "$HOOK_FILE"

echo "Hook pre-commit instalado en $HOOK_FILE"
