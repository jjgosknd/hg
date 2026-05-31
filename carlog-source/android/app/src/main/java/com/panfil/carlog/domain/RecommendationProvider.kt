package com.panfil.carlog.domain

object RecommendationProvider {

    private data class Template(
        val title: String,
        val description: String,
        val mileageInterval: Int,
        val monthsInterval: Int,
    )

    private val templates = listOf(
        Template("Замена моторного масла", "Регулярная замена масла продлевает ресурс двигателя", 10_000, 12),
        Template("Замена масляного фильтра", "Менять вместе с маслом для чистоты смазки", 10_000, 12),
        Template("Замена воздушного фильтра", "Чистый фильтр улучшает динамику и снижает расход", 20_000, 24),
        Template("Замена салонного фильтра", "Обеспечивает чистый воздух в салоне", 15_000, 12),
        Template("Замена тормозных колодок", "Изношенные колодки увеличивают тормозной путь", 30_000, 0),
        Template("Замена тормозных дисков", "Проверяйте толщину и состояние поверхности", 60_000, 0),
        Template("Замена свечей зажигания", "Влияют на запуск и стабильность работы двигателя", 30_000, 0),
        Template("Замена охлаждающей жидкости", "Предотвращает перегрев и коррозию системы охлаждения", 60_000, 36),
        Template("Замена тормозной жидкости", "Старая жидкость теряет свойства и может закипеть", 40_000, 24),
        Template("Замена ремня ГРМ", "Обрыв ремня приводит к серьёзным повреждениям двигателя", 90_000, 60),
        Template("Замена трансмиссионного масла", "Поддерживает плавность переключений и ресурс КПП", 60_000, 48),
        Template("Проверка и ротация шин", "Равномерный износ увеличивает срок службы шин", 10_000, 12),
        Template("Развал-схождение", "Правильные углы снижают износ шин и улучшают управляемость", 20_000, 12),
        Template("Замена топливного фильтра", "Чистый фильтр защищает форсунки и топливный насос", 40_000, 24),
        Template("Диагностика подвески", "Своевременная проверка предотвращает дорогой ремонт", 30_000, 24),
    )

    fun generate(
        currentMileage: Int,
        existingMaintenance: List<Maintenance>,
    ): List<MaintenanceRecommendation> {
        if (currentMileage <= 0) return emptyList()

        return templates.map { t ->
            val matching = existingMaintenance.filter {
                it.title.contains(t.title, ignoreCase = true) ||
                    t.title.contains(it.title, ignoreCase = true)
            }
            val lastMileage = matching.maxOfOrNull { it.lastMileage } ?: 0
            val hasRecord = lastMileage > 0

            // Базовая точка отсчёта (пробег последней замены).
            // Если записи о ТО ещё нет — не считаем пункт просроченным с нуля,
            // а привязываемся к текущему циклу пробега: берём ближайшую
            // «границу интервала» ниже текущего пробега. Так остаток до замены
            // всегда пересчитывается от актуального пробега.
            val baseline = if (hasRecord) {
                lastMileage
            } else if (t.mileageInterval > 0) {
                currentMileage - (currentMileage % t.mileageInterval)
            } else {
                currentMileage
            }

            val nextDue = baseline + t.mileageInterval
            val remaining = nextDue - currentMileage

            val status = when {
                // Просрочено только если есть реальная запись о прошлой замене.
                hasRecord && remaining <= 0 -> RecommendationStatus.OVERDUE
                remaining <= t.mileageInterval * 0.15 -> RecommendationStatus.UPCOMING
                else -> RecommendationStatus.OK
            }

            val progress = if (t.mileageInterval > 0) {
                ((currentMileage - baseline).toFloat() / t.mileageInterval).coerceIn(0f, 1f)
            } else 0f

            MaintenanceRecommendation(
                title = t.title,
                description = t.description,
                mileageInterval = t.mileageInterval,
                monthsInterval = t.monthsInterval,
                status = status,
                nextDueMileage = nextDue,
                overdueByKm = if (remaining < 0) -remaining else 0,
                remainingKm = remaining,
                progress = progress,
                hasRecord = hasRecord,
            )
        }.sortedBy { it.status.ordinal }
    }
}
