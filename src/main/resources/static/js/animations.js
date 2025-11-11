// Train Animation and Parallax Effects

document.addEventListener('DOMContentLoaded', function() {
    // Create animated train
    createAnimatedTrain();
    
    // Parallax scrolling effect
    initParallaxScrolling();
    
    // Scroll animations for elements
    initScrollAnimations();
    
    // Add ripple effect to buttons
    addRippleEffects();
});

// Create animated train that moves across screen
function createAnimatedTrain() {
    const trainContainer = document.createElement('div');
    trainContainer.className = 'train-container';
    trainContainer.innerHTML = `
        <div class="animated-train">
            <div class="train-smoke"></div>
            <div class="train-body">
                <div class="train-engine">
                    <div class="train-window"></div>
                    <div class="train-window"></div>
                </div>
                <div class="train-carriage"></div>
            </div>
            <div class="train-wheels">
                <div class="wheel"></div>
                <div class="wheel"></div>
            </div>
        </div>
    `;
    
    document.body.appendChild(trainContainer);
    
    // Make train follow scroll progress - only show when scrolling
    let scrollTimeout;
    window.addEventListener('scroll', () => {
        const windowHeight = document.documentElement.scrollHeight - document.documentElement.clientHeight;
        const scrolled = (window.pageYOffset / windowHeight) * 100;
        
        // Only show train if user has scrolled at least 5%
        if (scrolled > 5) {
            trainContainer.classList.add('visible');
            
            const trainPosition = (scrolled / 100) * (window.innerWidth - 60);
            const train = document.querySelector('.animated-train');
            if (train) {
                train.style.transform = `translateX(${trainPosition}px) scale(0.3)`;
            }
            
            // Hide train after 2 seconds of no scrolling
            clearTimeout(scrollTimeout);
            scrollTimeout = setTimeout(() => {
                trainContainer.classList.remove('visible');
            }, 1500);
        } else {
            trainContainer.classList.remove('visible');
        }
    });
}

// Parallax scrolling effect
function initParallaxScrolling() {
    window.addEventListener('scroll', function() {
        const scrolled = window.pageYOffset;
        
        // Parallax for hero banner
        const hero = document.querySelector('.hero-banner');
        if (hero) {
            hero.style.transform = `translateY(${scrolled * 0.5}px)`;
        }
        
        // Parallax for cards
        const cards = document.querySelectorAll('.info-card');
        cards.forEach((card, index) => {
            const speed = 0.1 + (index * 0.05);
            const yPos = -(scrolled * speed);
            card.style.transform = `translateY(${yPos}px)`;
        });
        
        // Parallax for feature cards
        const features = document.querySelectorAll('.feature-card');
        features.forEach((feature, index) => {
            const rect = feature.getBoundingClientRect();
            const inView = rect.top < window.innerHeight && rect.bottom > 0;
            
            if (inView) {
                const speed = 0.05 + (index * 0.02);
                const yPos = -(scrolled * speed);
                feature.style.transform = `translateY(${yPos}px)`;
            }
        });
    });
}

// Scroll animations - reveal elements on scroll
function initScrollAnimations() {
    const observerOptions = {
        threshold: 0.1,
        rootMargin: '0px 0px -50px 0px'
    };
    
    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.classList.add('visible');
                
                // Add stagger animation to children
                const children = entry.target.querySelectorAll('.animate-child');
                children.forEach((child, index) => {
                    setTimeout(() => {
                        child.classList.add('visible');
                    }, index * 50);
                });
            }
        });
    }, observerOptions);
    
    // Observe elements
    const animatedElements = document.querySelectorAll('.info-card, .feature-card, .journey-planner');
    animatedElements.forEach(el => observer.observe(el));
}

// Add ripple effect to buttons
function addRippleEffects() {
    const buttons = document.querySelectorAll('.btn-search, .tab-btn, button');
    
    buttons.forEach(button => {
        button.addEventListener('click', function(e) {
            const ripple = document.createElement('span');
            ripple.classList.add('ripple');
            
            const rect = this.getBoundingClientRect();
            const size = Math.max(rect.width, rect.height);
            const x = e.clientX - rect.left - size / 2;
            const y = e.clientY - rect.top - size / 2;
            
            ripple.style.width = ripple.style.height = size + 'px';
            ripple.style.left = x + 'px';
            ripple.style.top = y + 'px';
            
            this.appendChild(ripple);
            
            setTimeout(() => {
                ripple.remove();
            }, 600);
        });
    });
}

// Smooth scroll for anchor links
document.querySelectorAll('a[href^="#"]').forEach(anchor => {
    anchor.addEventListener('click', function(e) {
        e.preventDefault();
        const target = document.querySelector(this.getAttribute('href'));
        if (target) {
            target.scrollIntoView({
                behavior: 'smooth',
                block: 'start'
            });
        }
    });
});

// Add hover effect to navigation items
const navItems = document.querySelectorAll('.nav-menu a');
navItems.forEach(item => {
    item.addEventListener('mouseenter', function() {
        this.style.transform = 'translateY(-3px)';
    });
    
    item.addEventListener('mouseleave', function() {
        this.style.transform = 'translateY(0)';
    });
});

// Add loading animation
window.addEventListener('load', function() {
    document.body.classList.add('loaded');
    
    // Trigger initial animations
    setTimeout(() => {
        const hero = document.querySelector('.hero-content');
        if (hero) {
            hero.classList.add('animated');
        }
    }, 500);
});

// Add floating labels effect for inputs
const inputs = document.querySelectorAll('input[type="text"], input[type="date"]');
inputs.forEach(input => {
    input.addEventListener('focus', function() {
        this.parentElement.classList.add('focused');
    });
    
    input.addEventListener('blur', function() {
        if (!this.value) {
            this.parentElement.classList.remove('focused');
        }
    });
});

// Add progress bar on scroll
function createScrollProgress() {
    const progressBar = document.createElement('div');
    progressBar.className = 'scroll-progress';
    document.body.appendChild(progressBar);
    
    window.addEventListener('scroll', () => {
        const windowHeight = document.documentElement.scrollHeight - document.documentElement.clientHeight;
        const scrolled = (window.pageYOffset / windowHeight) * 100;
        progressBar.style.width = scrolled + '%';
    });
}

createScrollProgress();
