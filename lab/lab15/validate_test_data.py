import csv
from collections import Counter

def validate_synthetic_data(filename='synthetic_users.csv'):
    """Synthetic test data-г шалгах validation функц"""
    
    print("=" * 60)
    print("SYNTHETIC TEST DATA VALIDATION")
    print("=" * 60)
    
    with open(filename, 'r', encoding='utf-8') as f:
        reader = csv.DictReader(f)
        data = list(reader)
    
    print(f"\n✓ Нийт мөр тоо: {len(data)}")
    
    # 1. ДАВХАРДАЛ ШАЛГАХ
    print("\n" + "=" * 60)
    print("1. ДАВХАРДАЛ ШАЛГАЛТ")
    print("=" * 60)
    
    emails = [row['email'] for row in data]
    names = [row['name'] for row in data]
    
    email_duplicates = [email for email, count in Counter(emails).items() if count > 1]
    name_duplicates = [name for name, count in Counter(names).items() if count > 1]
    
    if email_duplicates:
        print(f" Давхардсан имэйл: {email_duplicates}")
    else:
        print("✓ Имэйл давхардаагүй")
    
    if name_duplicates:
        print(f" Давхардсан нэр: {name_duplicates}")
    else:
        print("✓ Нэр давхардаагүй")
    
    # 2. ЛОГИК ШАЛГАХ
    print("\n" + "=" * 60)
    print("2. ЛОГИК ШАЛГАЛТ")
    print("=" * 60)
    
    age_issues = []
    email_issues = []
    
    for row in data:
        age = int(row['age'])
        email = row['email']
        
        # Насны хүчинтэй байдал
        if age < 18 or age > 65:
            age_issues.append(f"{row['name']}: {age}")
        
        # Имэйл форматын шалгалт
        if '@' not in email or '.' not in email:
            email_issues.append(email)
    
    if age_issues:
        print(f" Хүчингүй нас: {age_issues}")
    else:
        print("✓ Нас хүчинтэй (18-65)")
    
    if email_issues:
        print(f" Буруу имэйл формат: {email_issues}")
    else:
        print("✓ Имэйл формат зөв")
    
    # 3. ТӨРЛИЙН ТАРХАЛТ
    print("\n" + "=" * 60)
    print("3. ӨГӨГДЛИЙН ТАРХАЛТ")
    print("=" * 60)
    
    countries = Counter([row['country'] for row in data])
    roles = Counter([row['role'] for row in data])
    
    print("\nУлс орны тоо:")
    for country, count in countries.most_common():
        print(f"  {country}: {count} ({count/len(data)*100:.1f}%)")
    
    print("\nРолийн тоо:")
    for role, count in roles.most_common():
        print(f"  {role}: {count} ({count/len(data)*100:.1f}%)")
    
    # 4. СТАТИСТИК МЭДЭЭЛЭЛ
    print("\n" + "=" * 60)
    print("4. НАСНЫ СТАТИСТИК")
    print("=" * 60)
    
    ages = [int(row['age']) for row in data]
    avg_age = sum(ages) / len(ages)
    min_age = min(ages)
    max_age = max(ages)
    
    print(f"Дундаж нас: {avg_age:.1f}")
    print(f"Хамгийн бага нас: {min_age}")
    print(f"Хамгийн их нас: {max_age}")
    
    # 5. ДҮГНЭЛТ
    print("\n" + "=" * 60)
    print("5. ЭЦСИЙН ДҮГНЭЛТ")
    print("=" * 60)
    
    issues = []
    if email_duplicates: issues.append("Имэйл давхардсан")
    if name_duplicates: issues.append("Нэр давхардсан")
    if age_issues: issues.append("Хүчингүй нас")
    if email_issues: issues.append("Буруу имэйл")
    
    if issues:
        print(f" Олдсон асуудал: {', '.join(issues)}")
        print("Заавар: AI-д дахин prompt өгч засуулна уу")
    else:
        print("✓ Өгөгдөл чанартай, тест хийхэд бэлэн!")
    
    print("=" * 60)


# Ажиллуулах
if __name__ == "__main__":
    try:
        validate_synthetic_data('synthetic_users.csv')
    except FileNotFoundError:
        print(" synthetic_users.csv файл олдсонгүй!")
        print("Заавар: Эхлээд CSV файлыг үүсгэнэ үү.")
    except Exception as e:
        print(f" Алдаа гарлаа: {e}")