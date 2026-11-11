<#
.SYNOPSIS
    Builds Bomberman into a standalone Windows app (Bomberman.exe + bundled Java runtime).

.DESCRIPTION
    1. Downloads LWJGL 3.1.2 + JOML from Maven Central into build/lib (cached).
    2. Compiles the sources with javac.
    3. Packs bomberman.jar and runs jpackage to create an app image with a trimmed JRE.
    4. Copies game data (levels, music, saves) next to the exe and zips everything.

    Requires a JDK 17+ (needs jpackage). The script looks at JAVA_HOME first,
    then at ~/.jdks and the usual install folders.

.EXAMPLE
    powershell -ExecutionPolicy Bypass -File build.ps1
    # -> build/Bomberman-windows-x64.zip  (unzipped: build/dist/Bomberman/Bomberman.exe)
#>
param(
    [string]$Version = "1.6.0"
)

$ErrorActionPreference = "Stop"
$ProgressPreference = "SilentlyContinue"   # makes Invoke-WebRequest much faster

$Root  = $PSScriptRoot
$Build = Join-Path $Root "build"
$Lib   = Join-Path $Build "lib"

# ---------------------------------------------------------------- JDK
function Find-Jdk {
    $candidates = @()
    if ($env:JAVA_HOME) { $candidates += $env:JAVA_HOME }
    foreach ($dir in @("$env:USERPROFILE\.jdks", "$env:ProgramFiles\Java",
                       "$env:ProgramFiles\Eclipse Adoptium", "$env:ProgramFiles\Microsoft")) {
        if (Test-Path $dir) {
            $candidates += Get-ChildItem $dir -Directory | Sort-Object Name -Descending | ForEach-Object FullName
        }
    }
    foreach ($c in $candidates) {
        if (Test-Path (Join-Path $c "bin\jpackage.exe")) { return $c }
    }
    throw "No JDK with jpackage found. Install JDK 17+ and set JAVA_HOME."
}
$Jdk = Find-Jdk
Write-Host "Using JDK: $Jdk"
$javac    = Join-Path $Jdk "bin\javac.exe"
$jar      = Join-Path $Jdk "bin\jar.exe"
$jpackage = Join-Path $Jdk "bin\jpackage.exe"

# ---------------------------------------------------------------- dependencies
$Maven = "https://repo1.maven.org/maven2"
$deps = @(
    "org/lwjgl/lwjgl/3.1.2/lwjgl-3.1.2.jar",
    "org/lwjgl/lwjgl/3.1.2/lwjgl-3.1.2-natives-windows.jar",
    "org/lwjgl/lwjgl-glfw/3.1.2/lwjgl-glfw-3.1.2.jar",
    "org/lwjgl/lwjgl-glfw/3.1.2/lwjgl-glfw-3.1.2-natives-windows.jar",
    "org/lwjgl/lwjgl-opengl/3.1.2/lwjgl-opengl-3.1.2.jar",
    "org/lwjgl/lwjgl-opengl/3.1.2/lwjgl-opengl-3.1.2-natives-windows.jar",
    "org/lwjgl/lwjgl-stb/3.1.2/lwjgl-stb-3.1.2.jar",
    "org/lwjgl/lwjgl-stb/3.1.2/lwjgl-stb-3.1.2-natives-windows.jar",
    "org/joml/joml/1.9.25/joml-1.9.25.jar"
)
New-Item -ItemType Directory -Force $Lib | Out-Null
foreach ($d in $deps) {
    $file = Join-Path $Lib (Split-Path $d -Leaf)
    if (-not (Test-Path $file)) {
        Write-Host "Downloading $(Split-Path $d -Leaf)"
        Invoke-WebRequest "$Maven/$d" -OutFile $file
    }
}
$libJars = Get-ChildItem $Lib -Filter *.jar | Sort-Object Name

# ---------------------------------------------------------------- compile
$Classes = Join-Path $Build "classes"
$App     = Join-Path $Build "app"
$Dist    = Join-Path $Build "dist"
foreach ($d in @($Classes, $App, $Dist)) {
    if (Test-Path $d) { Remove-Item -Recurse -Force $d }
    New-Item -ItemType Directory -Force $d | Out-Null
}

Write-Host "Compiling..."
$sources = Get-ChildItem (Join-Path $Root "src\main\java") -Recurse -Filter *.java | ForEach-Object FullName
$cp = ($libJars | ForEach-Object FullName) -join ";"
& $javac --release 17 -encoding UTF-8 -nowarn -cp $cp -d $Classes @sources
if ($LASTEXITCODE -ne 0) { throw "javac failed" }

# ---------------------------------------------------------------- jar
# Textures/shaders are read through File, so they must stay a plain directory
# on the classpath (resources/) instead of being packed inside the jar.
New-Item -ItemType Directory -Force (Join-Path $App "lib") | Out-Null
$libJars | Copy-Item -Destination (Join-Path $App "lib")
Copy-Item -Recurse (Join-Path $Root "src\main\resources") (Join-Path $App "resources")

$manifest = Join-Path $Build "MANIFEST.MF"
$classPath = @("resources/") + ($libJars | ForEach-Object { "lib/$($_.Name)" })
# Manifest lines are limited to 72 bytes - continuation lines start with a space.
$cpLine = "Class-Path: " + ($classPath -join " ")
$wrapped = for ($i = 0; $i -lt $cpLine.Length; $i += 70) {
    $chunk = $cpLine.Substring($i, [Math]::Min(70, $cpLine.Length - $i))
    if ($i -eq 0) { $chunk } else { " " + $chunk }
}
(@("Main-Class: com.bomberman.game.Main") + $wrapped + "") -join "`r`n" |
    Set-Content -Encoding ascii -NoNewline $manifest
Add-Content -Encoding ascii -Value "" $manifest

& $jar --create --file (Join-Path $App "bomberman.jar") --manifest $manifest -C $Classes .
if ($LASTEXITCODE -ne 0) { throw "jar failed" }

# ---------------------------------------------------------------- jpackage
Write-Host "Packaging with jpackage..."
& $jpackage --type app-image `
    --name Bomberman `
    --app-version $Version `
    --input $App `
    --main-jar bomberman.jar `
    --main-class com.bomberman.game.Main `
    --add-modules java.base,java.desktop,jdk.unsupported `
    --jlink-options "--strip-debug --no-header-files --no-man-pages --strip-native-commands" `
    --dest $Dist
if ($LASTEXITCODE -ne 0) { throw "jpackage failed" }

# ---------------------------------------------------------------- game data
# The game opens levels/, music/, saves/ and Wyniki.bin relative to the working
# directory, which is the exe folder when the game is started by double-click.
$Image = Join-Path $Dist "Bomberman"
Copy-Item -Recurse (Join-Path $Root "levels") (Join-Path $Image "levels")
Copy-Item -Recurse (Join-Path $Root "saves")  (Join-Path $Image "saves")
New-Item -ItemType Directory -Force (Join-Path $Image "music") | Out-Null
Get-ChildItem (Join-Path $Root "music") -Include *.ogg, *.wav -Recurse |
    Copy-Item -Destination (Join-Path $Image "music")
Copy-Item (Join-Path $Root "Wyniki.bin") $Image
New-Item -ItemType Directory -Force (Join-Path $Image "screenshots") | Out-Null

# ---------------------------------------------------------------- zip
$Zip = Join-Path $Build "Bomberman-windows-x64.zip"
if (Test-Path $Zip) { Remove-Item -Force $Zip }
Compress-Archive -Path $Image -DestinationPath $Zip -CompressionLevel Optimal

$sizeMb = [Math]::Round((Get-Item $Zip).Length / 1MB, 1)
Write-Host ""
Write-Host "Done:"
Write-Host "  exe: $(Join-Path $Image 'Bomberman.exe')"
Write-Host "  zip: $Zip ($sizeMb MB)"
