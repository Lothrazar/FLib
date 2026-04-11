#!/usr/bin/env bash
# this set -e tells bash to exit if a command such as git clone fails (nonzero)
set -e

GITHUB="Lothrazar"
TEMP_DIR="/c/temp"
SOURCE_DIR="/c/Users/USER/eclipse-workspace/mc121/FLib"

REPONAME="$1"
if [ -z "$REPONAME" ]; then
    echo "Usage: clone.sh <repo-name>"
    exit 1
fi

MC="1.21.1"
TARGET_DIR="/c/Users/USER/MyFiles/mc121/$REPONAME"
GIT_URL="git@github.com:${GITHUB}/${REPONAME}.git"
MOD_PROPS="$TARGET_DIR/mod.properties"
DEPLOY_PROPS="$TARGET_DIR/deploy.properties"
FLIB_VERSION="1.0.0-SNAPSHOT"

echo
git clone "$GIT_URL" "$TARGET_DIR"
echo

echo "✓  Cloned '$REPONAME' repository"


cd "$TARGET_DIR"


if [ ! -f "$MOD_PROPS" ]; then

  cat > "$MOD_PROPS" << EOF

flib_version=$FLIB_VERSION

EOF


  echo "✓  mod.properties not found, template created"

  grep -E "^(mod_version|mod_id|mod_name|mod_license|mod_authors|mod_description|mod_group_id|curse_id|curse_slug)=" "$TARGET_DIR/gradle.properties" >> "$MOD_PROPS" || true

  echo "✓  Moved mod-specific properties into $MOD_PROPS"

fi

if [ ! -f "$DEPLOY_PROPS" ]; then

  cat > "$DEPLOY_PROPS" << EOF

# CSV list of folders that will copy the release version to
# after the gradle publish task

destinations=C:/temp

EOF
  echo "✓  deploy.properties not found, template created"
fi

cp     "$SOURCE_DIR/.gitignore"          ./.gitignore
# gradle and other core shared files copied
cp -r  "$SOURCE_DIR/gradle"              ./
cp     "$SOURCE_DIR/gradle.properties"   ./gradle.properties
cp     "$SOURCE_DIR/settings.gradle"     ./settings.gradle
cp     "$SOURCE_DIR/build.gradle"        ./build.gradle
cp     "$SOURCE_DIR/gradlew"             ./gradlew
cp     "$SOURCE_DIR/gradlew.bat"         ./gradlew.bat
echo "✓  Updated gradle"

# rm legacy mods.toml if present (NeoForge uses templates -> neoforge.mods.toml)
rm -f  ./src/main/resources/META-INF/mods.toml
echo "✓  Removed legacy mods.toml"

# add the new template
cp -r  "$SOURCE_DIR/src/main/templates"  ./src/main/templates

MODS_TOML="$TARGET_DIR/src/main/templates/META-INF/neoforge.mods.toml"
if [ -f "$MODS_TOML" ]; then
    cat >> "$MODS_TOML" << 'EOF'

[[dependencies.${mod_id}]]
    modId="flib"
    type="required"
    versionRange="[0.1.0,)"
    ordering="NONE"
    side="BOTH"
EOF

    echo "✓  Appended flib dependency to neoforge.mods.toml"
fi



mkdir  "$TARGET_DIR/libs"
cp     "/c/temp/flib-$MC-$FLIB_VERSION.jar"  "$TARGET_DIR/libs/flib-$MC-$FLIB_VERSION.jar"

echo "✓  Default library copied from temp"

echo "✓  Source branch $(git rev-parse --abbrev-ref HEAD)"

echo "!  Verify mod.properties values and optional dependencies before building  !"

echo "!  Update build.gradle dependencies as needed"

echo

git checkout -b port

echo
# read -rp "Press Enter to close..."
