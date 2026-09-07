import os, base64

def wf(path, content):
    d = os.path.dirname(path)
    if d: os.makedirs(d, exist_ok=True)
    with open(path, "w", encoding="utf-8") as f:
        f.write(content)
    print(f"OK {path}")

def wf64(path, b64):
    wf(path, base64.b64decode(b64).decode("utf-8"))

