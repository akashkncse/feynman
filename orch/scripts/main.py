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
        img_dir = os.path.join(fp, "images")
        if not os.path.exists(ap) or not os.path.exists(mp):
            print(ap, mp)
            failed += 1
            continue
        opened_image_files = []
        try:
            with (
                open(mp, "r", encoding="utf-8") as md,
                open(ap, "rb") as a,
            ):
                files = [
                    ("data", (None, md.read(), "application/json")),
                    ("file", ("audio.wav", a, "audio/wav"))
                ]
                if os.path.exists(img_dir) and os.path.isdir(img_dir):
                    valid_extensions = (".png", ".jpg", ".jpeg", ".webp")
                    for img_name in os.listdir(img_dir):
                        if img_name.lower().endswith(valid_extensions):
                            img_path = os.path.join(img_dir, img_name)
                            img_file = open(img_path, "rb")
                            opened_image_files.append(img_file)
                            files.append(("images", (img_name, img_file, "image/*")))
                res = requests.post(URL, files=files)
                if res.status_code == 200:
                    success += 1
                    continue
                else:
                    failed += 1
        except:
            failed += 1
        finally:
            for img_file in opened_image_files:
                img_file.close()
    print(f"{success} payloads hit! {failed} failed to hit")
if __name__ == "__main__":
    test()