import os


def resource_path(relative_path: str) -> str:
    """
    Resolve an asset path relative to this file's directory and normalize
    separators so it works on macOS/Linux/Windows.
    """
    base_dir = os.path.dirname(__file__)
    normalized = relative_path.replace("\\", "/")
    # Split to avoid accidental double separators and join with OS-specific sep
    return os.path.join(base_dir, *[p for p in normalized.split("/") if p])
