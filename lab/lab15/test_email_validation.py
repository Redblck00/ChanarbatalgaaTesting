import pytest

def is_valid_email(email):
    """Имэйл хаягийн зөв эсэхийг шалгах функц"""
    if "@" not in email:
        return False
    if email.endswith("@test.com"):
        return False
    return True


class TestEmailValidation:
    """Email validation функцийн unit тестүүд"""
    
    # ЭЕРЭГ ТЕСТҮҮД
    def test_valid_email_with_gmail(self):
        """Gmail хаяг зөв ажиллах ёстой"""
        assert is_valid_email("user@gmail.com") == True
    
    def test_valid_email_with_corporate_domain(self):
        """Компанийн домэйн зөв ажиллах ёстой"""
        assert is_valid_email("employee@company.mn") == True
    
    def test_valid_email_with_subdomain(self):
        """Subdomain агуулсан имэйл зөв байх ёстой"""
        assert is_valid_email("user@mail.example.com") == True
    
    # СӨРӨГ ТЕСТҮҮД
    def test_invalid_email_without_at_symbol(self):
        """@ тэмдэггүй имэйл буруу байх ёстой"""
        assert is_valid_email("usergmail.com") == False
    
    def test_invalid_email_with_test_domain(self):
        """@test.com төгсгөлтэй имэйл буруу байх ёстой"""
        assert is_valid_email("user@test.com") == False
    
    def test_invalid_email_only_at_symbol(self):
        """Зөвхөн @ тэмдэг агуулсан текст буруу байх ёстой"""
        assert is_valid_email("@") == False
    
    # EDGE CASE ТЕСТҮҮД
    def test_empty_string(self):
        """Хоосон string буруу байх ёстой"""
        assert is_valid_email("") == False
    
    def test_email_with_multiple_at_symbols(self):
        """Олон @ тэмдэгтэй имэйл зөв эсэхийг шалгах"""
        assert is_valid_email("user@@example.com") == True  # @ агуулсан тул True
    
    def test_email_starting_with_at(self):
        """@ тэмдгээр эхэлсэн имэйл"""
        assert is_valid_email("@example.com") == True
    
    def test_email_with_spaces(self):
        """Зайтай имэйл"""
        assert is_valid_email("user @example.com") == True  # @ агуулсан
    
    # BOUNDARY ТЕСТҮҮД
    def test_very_long_email(self):
        """Маш урт имэйл хаяг"""
        long_email = "a" * 100 + "@example.com"
        assert is_valid_email(long_email) == True
    
    def test_single_char_before_at(self):
        """@ өмнө нэг тэмдэгт"""
        assert is_valid_email("a@example.com") == True
    
    def test_email_ending_with_test_com_different_case(self):
        """@TEST.COM (том үсгээр)"""
        # Одоогийн функц case-sensitive тул зөв гарна
        assert is_valid_email("user@TEST.COM") == True


# Тест ажиллуулах заавар:
# 1. Terminal дээр: pip install pytest
# 2. Файлыг хадгалаад: pytest test_email_validation.py -v
# 3. Дэлгэрэнгүй үр дүн харах: pytest test_email_validation.py -v -s

if __name__ == "__main__":
    pytest.main([__file__, "-v"])