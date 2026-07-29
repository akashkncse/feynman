import os
from pathlib import Path
import requests

URL = "http://localhost:8080/orch"
SCRIPT = os.path.dirname(os.path.abspath(__file__))
PAYLOAD = os.path.join(SCRIPT, "payload")

def test():
    if not os.path.exists(PAYLOAD):
        print("No payload directory found, ensure that you create payloads in root")
        return
    folders = [
        f
        for f in os.listdir(PAYLOAD)
        if os.path.isdir(os.path.join(PAYLOAD, f))
    ]
    folders.sort(
        key=lambda name: int(name) if name.isdigit() else name
    )
    print(f"Found {len(folders)} payloads, hitting API Now")
    success = 0
    failed = 0
    for f in folders:
        fp = os.path.join(PAYLOAD, f)
        ap = os.path.join(fp, "audio.wav")
        mp = os.path.join(fp, "metadata.json")
        if not os.path.exists(ap) or not os.path.exists(mp):
            print(ap, mp)
            failed += 1
            continue
        with (
            open(mp, "r", encoding="utf-8") as md,
            open(ap, "rb") as a,
        ):
            files = {
                "data": (None, md.read(), "application/json"),
                "file": ("audio.wav", a, "audio/wav")
            }
            try:
                res = requests.post(URL, files=files)
                if (res.status_code == 200):
                    success += 1
                    continue
            except:
                failed += 1
    print(f"{success} payloads hit! {failed} failed to hit")
if __name__ == "__main__":
    test()