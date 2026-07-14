import sys
import os
import tempfile
import zipfile
import shutil
import subprocess

from pathlib import Path


def print_error_message(message: str):
    if not message.startswith('error: '):
        print(f'\nerror: {message}')
    else:
        print(f'\n{message}')

    exit(1)


def check_directory(path: str) -> tuple[bool, str]:
    pth = Path(path)

    if not pth.exists():
        return False, "Path does not exists"

    if not pth.is_dir():
        return False, "Path is not a directory"

    try:
        with tempfile.TemporaryFile(dir=pth):
            pass
    except Exception as e:
        return False, f"Directory is not writable: {e}"

    return True, "Directory is valid and writable"


def mkdir_libs_directory(base_path: str) -> tuple[bool, str]:
    path = Path(base_path)

    libs_path = path / "libs"

    if libs_path.exists():
        if libs_path.is_dir():
            return True, f"'libs' directory already exists: {libs_path}"
        
        return False, f"file named 'libs' already exists: {libs_path}"

    try:
        libs_path.mkdir()
        return True, f"'libs' directory created successfully: {libs_path}"
    except PermissionError:
        return False, f"permission denied, cannot create directory in {path}"
    except Exception as e:
        return False, f"failed to create libs directory: {e}"
    

def extract_dependency_file(zip_path: str, destination_path: str) -> tuple[bool, str]:
    zip_file = Path(zip_path)
    destination = Path(destination_path)

    if not zip_file.exists():
        return False, f"zip file does not exist: {zip_file}"

    if not zip_file.is_file():
        return False, f"zip path is not a file: {zip_file}"

    if not destination.exists():
        return False, f"destination path does not exist: {destination}"

    if not destination.is_dir():
        return False, f"destination path is not a directory: {destination}"

    try:
        with zipfile.ZipFile(zip_file, "r") as zf:
            zf.extractall(destination)
        return True, f"zip extracted successfully to: {destination}"
    except zipfile.BadZipFile:
        return False, f"invalid zip file: {zip_file}"
    except PermissionError:
        return False, f"permission denied while extracting to: {destination}"
    except Exception as e:
        return False, f"failed to extract zip file: {e}"
    

def copy_shared_libs() -> tuple[bool, str]:
    source_dir = Path('.') / "native" / "out"
    target_dirs = [Path("/lib"), Path("/lib64")]

    if not source_dir.exists():
        return False, f"source directory does not exist: {source_dir}"

    if not source_dir.is_dir():
        return False, f"source path is not a directory: {source_dir}"

    # Find the pattern (*.so*)
    so_files = [file for file in source_dir.rglob("*")if file.is_file() and (
        file.name.endswith(".so") or ".so." in file.name
        )
    ]

    if not so_files:
        return True, f"no shared library found to copy"

    copied_files = []

    try:
        for so_file in so_files:
            for target_dir in target_dirs:
                if target_dir.exists() and target_dir.is_dir():
                    destination = target_dir / so_file.name

                    shutil.copy2(so_file, destination)
                    copied_files.append(str(destination))

        if not copied_files:
            return False, "no valid target directories found"

        return True, f"copied {len(copied_files)} file(s) successfully"
    except PermissionError:
        return False, "permission denied while copying to /lib or /lib64. Try running with sudo."
    except Exception as e:
        return False, f"failed to copy shared libraries: {e}"

def update_linker_cache() -> tuple[bool, str]:
    try:
        subprocess.run(["ldconfig"], check=True)
        return True, "linker cache refreshed successfully"
    except Exception as e:
        return False, f"failed to run ldconfig: {e}"

# User aguments
args = sys.argv

# Project paths
project_paths = []

if os.geteuid() != 0:
    print('error: run script using superuser-privilege (root) user')
    exit(0)

print('Checking arguments               ', end='')
if len(args) == 1:
    print('[ERROR]')
    print_error_message('error: no project path specified')
else:
    print('[OK]')

# Remove the first arg from cli arguments
# Add items to the paths
for i in range(1, len(args)):
    project_paths.append(args[i])

print('Checking directories             ', end='')
for path in project_paths:
    result = check_directory(path)

    if not result[0]:
        print('[ERROR]')
        print_error_message(result[1])

print('[OK]')

print('Creating libs directories        ', end='')
for path in project_paths:
    result = mkdir_libs_directory(path)

    if not result[0]:
        print('[ERROR]')
        print_error_message(result[1])

print('[OK]')


print('Extracting dependency file       ', end='')
for path in project_paths:
    result = extract_dependency_file('dependency.zip', f'{path}/libs')

    if not result[0]:
        print('[ERROR]')
        print_error_message(result[1])

print('[OK]')

print('Copying native libs into system  ', end='')
result = copy_shared_libs()

if not result[0]:
    print_error_message(result[1])
else:
    print('[OK]')

# Some libs found, so update linker cache
if result[1] != 'no shared library found to copy':
    print('Updating linker cache            ', end='')

    result = update_linker_cache()
    if not result[0]:
        print_error_message(result[1])
    else:
        print('[OK]')


print('\nmessage: dependencies installed successfully.')
