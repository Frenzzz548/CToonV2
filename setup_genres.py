#!/usr/bin/env python3
"""
Setup genres and comic_genres tables for CToon
Maps existing comic categories to genres table
"""

import os
import mysql.connector
from mysql.connector import errorcode

DB_CONFIG = {
    'host': os.getenv('DB_HOST', 'localhost'),
    'port': int(os.getenv('DB_PORT', 3306)),
    'user': os.getenv('DB_USER', 'root'),
    'password': os.getenv('DB_PASSWORD', ''),
    'database': os.getenv('DB_NAME', 'ctoon')
}

def slugify(text):
    """Convert text to slug format"""
    return text.lower().replace(' ', '-')

def setup_genres():
    """Create genres table and populate with initial genres"""
    try:
        conn = mysql.connector.connect(**DB_CONFIG)
        cursor = conn.cursor(dictionary=True)
        
        print("Setting up genres...")
        
        # Create genres table if it doesn't exist
        create_genres = """
        CREATE TABLE IF NOT EXISTS genres (
            id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
            name VARCHAR(255) NOT NULL UNIQUE,
            slug VARCHAR(255) NOT NULL UNIQUE,
            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
        )
        """
        cursor.execute(create_genres)
        conn.commit()
        print("✓ Genres table ready")
        
        # Create comic_genres junction table if it doesn't exist
        create_junction = """
        CREATE TABLE IF NOT EXISTS comic_genres (
            id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
            comic_id INT NOT NULL,
            genre_id BIGINT UNSIGNED NOT NULL,
            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            FOREIGN KEY (comic_id) REFERENCES comics(id) ON DELETE CASCADE,
            FOREIGN KEY (genre_id) REFERENCES genres(id) ON DELETE CASCADE,
            UNIQUE KEY unique_comic_genre (comic_id, genre_id)
        )
        """
        cursor.execute(create_junction)
        conn.commit()
        print("✓ Comic_genres junction table ready")
        
        # Insert initial genres if they don't exist
        genres_list = [
            ('Action', 'action'),
            ('Romance', 'romance'),
            ('Fantasy', 'fantasy'),
            ('Comedy', 'comedy'),
            ('Drama', 'drama'),
            ('Adventure', 'adventure'),
            ('Mystery', 'mystery'),
            ('Thriller', 'thriller'),
            ('Sci-Fi', 'sci-fi'),
            ('Horror', 'horror'),
        ]
        
        for name, slug in genres_list:
            try:
                insert_genre = "INSERT INTO genres (name, slug) VALUES (%s, %s)"
                cursor.execute(insert_genre, (name, slug))
            except mysql.connector.Error as err:
                if err.errno == 1062:  # Duplicate entry
                    pass
                else:
                    raise
        
        conn.commit()
        print("✓ Genres populated")
        
        # Map existing comics to genres based on their category
        print("\nMapping comics to genres...")
        
        # Get all comics with their categories
        cursor.execute("SELECT id, category FROM comics WHERE category IS NOT NULL")
        comics = cursor.fetchall()
        
        for comic in comics:
            comic_id = comic['id']
            category = comic['category']
            
            # Get the genre ID for this category
            cursor.execute("SELECT id FROM genres WHERE name = %s", (category,))
            genre = cursor.fetchone()
            
            if genre:
                genre_id = genre['id']
                try:
                    insert_mapping = """
                    INSERT INTO comic_genres (comic_id, genre_id) VALUES (%s, %s)
                    ON DUPLICATE KEY UPDATE created_at = created_at
                    """
                    cursor.execute(insert_mapping, (comic_id, genre_id))
                    print(f"  ✓ Comic ID {comic_id} -> Genre '{category}'")
                except mysql.connector.Error as err:
                    if err.errno == 1062:  # Duplicate entry
                        pass
                    else:
                        print(f"  ✗ Error: {err}")
        
        conn.commit()
        print("\n✓ Setup complete!")
        
        cursor.close()
        conn.close()
        
    except mysql.connector.Error as err:
        if err.errno == errorcode.ER_ACCESS_DENIED_ERROR:
            print("✗ Invalid username or password")
        elif err.errno == errorcode.ER_BAD_DB_ERROR:
            print("✗ Database does not exist")
        else:
            print(f"✗ Error: {err}")
        return False
    
    return True

if __name__ == '__main__':
    setup_genres()
